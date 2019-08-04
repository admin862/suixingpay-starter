package com.suixingpay.takin.id;

/**
 * 
 * @author renjinhao
 *
 */
public interface IdGenerator {

	String generate(String namespace) throws IdGenerateException;

	String generate(String namespace, String bizMark) throws IdGenerateException;
}
