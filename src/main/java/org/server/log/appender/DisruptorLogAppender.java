package org.server.log.appender;

import org.server.log.appender.disruptor.LogProducerTranslator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 自定义异步记录日志Appender. 替换掉以前的BlockingQueue队列，改用Disruptor无锁内存队列.
 * 
 * @author 刘源
 * 
 */
public class DisruptorLogAppender extends
		DisruptorLogAppenderBase<ILoggingEvent> {

	boolean includeCallerData = false;

	/**
	 * Events of level TRACE, DEBUG and INFO are deemed to be discardable.
	 * 
	 * @param event
	 * @return true if the event is of level TRACE, DEBUG or INFO false
	 *         otherwise.
	 */
	protected boolean isDiscardable(ILoggingEvent event) {
		Level level = event.getLevel();
		return level.toInt() <= Level.INFO_INT;
	}

	protected void preprocess(ILoggingEvent eventObject) {
		eventObject.prepareForDeferredProcessing();
		if (includeCallerData)
			eventObject.getCallerData();
	}

	protected void put(ILoggingEvent event) {
		ringBuffer.publishEvent(LogProducerTranslator.TRANSLATOR, event,aai);
	}

	public boolean isIncludeCallerData() {
		return includeCallerData;
	}

	public void setIncludeCallerData(boolean includeCallerData) {
		this.includeCallerData = includeCallerData;
	}

}
