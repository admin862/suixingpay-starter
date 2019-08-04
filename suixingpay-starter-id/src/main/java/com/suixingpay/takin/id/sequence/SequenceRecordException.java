package com.suixingpay.takin.id.sequence;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.type.ExceptionCode;

/**
 * 
 * @author renjinhao
 *
 */
public class SequenceRecordException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -736351637623065198L;

	public SequenceRecordException(String message) {
		super(ExceptionCode.INTERNAL_SERVER_ERROR, message);
	}
}
