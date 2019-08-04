package com.suixingpay.takin.id;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 
 * @author renjinhao
 *
 */
public class IdGenerateException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2116943338291651997L;

	public IdGenerateException(Exception e) {
		super(ExceptionCode.INTERNAL_SERVER_ERROR, e);
	}
}
