package org.server.log.appender;

import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.server.log.appender.disruptor.LogDisruptorEventHandle;
import org.server.log.appender.disruptor.LogValueEvent;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 
 * 自定义异步记录日志Appender.
 * 替换掉以前的BlockingQueue队列，改用Disruptor无锁内存队列.
 * @author 刘源
 */
public class DisruptorLogAppenderBase<E> extends UnsynchronizedAppenderBase<E>
		implements AppenderAttachable<E> {

	AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
	RingBuffer<LogValueEvent> ringBuffer;
	/**
	 * The default buffer size.
	 */
	public static final int DEFAULT_QUEUE_SIZE = 1024;
	int queueSize = DEFAULT_QUEUE_SIZE;

	int appenderCount = 0;

	static final int UNDEFINED = -1;
	int discardingThreshold = UNDEFINED;

	/**
	 * Is the eventObject passed as parameter discardable? The base class's
	 * implementation of this method always returns 'false' but sub-classes may
	 * (and do) override this method.
	 * <p/>
	 * <p>
	 * Note that only if the buffer is nearly full are events discarded.
	 * Otherwise, when the buffer is "not full" all events are logged.
	 * 
	 * @param eventObject
	 * @return - true if the event can be discarded, false otherwise
	 */
	protected boolean isDiscardable(E eventObject) {
		return false;
	}

	/**
	 * Pre-process the event prior to queueing. The base class does no
	 * pre-processing but sub-classes can override this behavior.
	 * 
	 * @param eventObject
	 */
	protected void preprocess(E eventObject) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start() {
		if (appenderCount == 0) {
			addError("No attached appenders found.");
			return;
		}
		if (queueSize < 1) {
			addError("Invalid queue size [" + queueSize + "]");
			return;
		}
		addInfo("环形缓冲区的大小： " + queueSize);
		Executor executor = Executors.newCachedThreadPool();
		Disruptor<LogValueEvent> disruptor = new Disruptor<LogValueEvent>(
				LogValueEvent.EVENT_FACTORY, queueSize, executor,
				ProducerType.MULTI, new SleepingWaitStrategy());
		disruptor.handleEventsWith(new LogDisruptorEventHandle());
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
		super.start();
	}

	@Override
	public void stop() {
		if (!isStarted())
			return;
		// mark this appender as stopped so that Worker can also
		// processPriorToRemoval if it is invoking aii.appendLoopOnAppenders
		// and sub-appenders consume the interruption
		super.stop();
	}

	@Override
	protected void append(E eventObject) {
		preprocess(eventObject);
		put(eventObject);
	}
	
	protected void put(E eventObject) {
	}
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public int getDiscardingThreshold() {
		return discardingThreshold;
	}

	public void setDiscardingThreshold(int discardingThreshold) {
		this.discardingThreshold = discardingThreshold;
	}

	public void addAppender(Appender<E> newAppender) {
			appenderCount++;
			addInfo("Attaching appender named [" + newAppender.getName()
					+ "] to DisruptorLogAppender.");
			aai.addAppender(newAppender);
	}

	public Iterator<Appender<E>> iteratorForAppenders() {
		return aai.iteratorForAppenders();
	}

	public Appender<E> getAppender(String name) {
		return aai.getAppender(name);
	}

	public boolean isAttached(Appender<E> eAppender) {
		return aai.isAttached(eAppender);
	}

	public void detachAndStopAllAppenders() {
		aai.detachAndStopAllAppenders();
	}

	public boolean detachAppender(Appender<E> eAppender) {
		return aai.detachAppender(eAppender);
	}

	public boolean detachAppender(String name) {
		return aai.detachAppender(name);
	}
}
