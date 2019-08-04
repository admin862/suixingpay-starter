package com.suixingpay.takin.id;

/**
 * 
 * @author renjinhao
 *
 */
public interface SequenceManager {
	
	public String getNextSequence(String namespace) throws Exception;
}
