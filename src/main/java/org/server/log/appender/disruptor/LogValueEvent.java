package org.server.log.appender.disruptor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

import com.lmax.disruptor.EventFactory;

/**
 * Disruptor生产者传递到消费者内容的载体.
 * 
 * @author 创建者:刘源
 */

public class LogValueEvent {
	public LogValueEvent() {

	}
	private ILoggingEvent eventObject;
	private AppenderAttachableImpl<ILoggingEvent> parent;
	public ILoggingEvent getEventObject() {
		return eventObject;
	}
	public void setEventObject(ILoggingEvent eventObject) {
		this.eventObject = eventObject;
	}

	public AppenderAttachableImpl<ILoggingEvent> getParent() {
		return parent;
	}

	public void setParent(AppenderAttachableImpl<ILoggingEvent> parent) {
		this.parent = parent;
	}

	
	/**
	 * 由于需要让Disruptor为我们创建事件，我们同时还声明了一个EventFactory来实例化Event对象.
	 */
	public final static EventFactory<LogValueEvent> EVENT_FACTORY = new EventFactory<LogValueEvent>() {
		@Override
		public LogValueEvent newInstance() {
			return new LogValueEvent();
		}
	};

}
