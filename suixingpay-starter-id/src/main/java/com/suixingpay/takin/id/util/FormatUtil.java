package com.suixingpay.takin.id.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author renjinhao
 *
 */
public class FormatUtil {

	private final static String DATE_FORMAT = "yyyyMMdd";
	private final static String FILL_CONTENT = "0";

	public static String getDateString() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
	}

	public static String formatSeq(BigDecimal seq, Integer length) {
		String seqStr = seq.toString();
		return formatSeq(seqStr, length);
	}
	
	public static String formatSeq(Long seq, Integer length) {
		String seqStr = seq.toString();
		return formatSeq(seqStr, length);
	}
	
	public static String formatSeq(Integer seq, Integer length) {
		String seqStr = seq.toString();
		return formatSeq(seqStr, length);
	}
	
	public static String formatSeq(String seqStr, Integer length) {
		StringBuilder sb = new StringBuilder();
		int offset = length - seqStr.length();
		for (int i = 0; i < offset; i++) {
			sb.append(FILL_CONTENT);
		}
		return sb.toString() + seqStr;
	}

}
