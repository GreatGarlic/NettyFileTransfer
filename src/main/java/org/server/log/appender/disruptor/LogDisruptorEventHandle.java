package org.server.log.appender.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;

/**
 * 消费者处理类.
 * 
 * @author 创建者:刘源
 */

public class LogDisruptorEventHandle implements EventHandler<LogValueEvent> {

	public LogDisruptorEventHandle() {

	}
	/**
	 * 
	 * 处理消费者要做的事.
	 * 
	 * @param event
	 *            event published to the {@link RingBuffer}
	 * @param sequence
	 *            sequence of the event being processed
	 * @param endOfBatch
	 *            endOfBatch flag to indicate if this is the last event in a batch from the {@link RingBuffer} ·
	 * 
	 *            方法添加日期 :2014年10月8日<br>
	 *            创建者:刘源
	 */
	@Override
	public void onEvent(LogValueEvent event, long sequence, boolean endOfBatch) {
		event.getParent().appendLoopOnAppenders(event.getEventObject());
		
	}

}
