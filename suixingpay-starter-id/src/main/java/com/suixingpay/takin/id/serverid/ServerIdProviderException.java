package com.suixingpay.takin.id.serverid;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 
 * @author renjinhao
 *
 */
public class ServerIdProviderException extends BaseException {

	public ServerIdProviderException(String message) {
		super(ExceptionCode.INTERNAL_SERVER_ERROR, message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6918819340907596923L;

}
