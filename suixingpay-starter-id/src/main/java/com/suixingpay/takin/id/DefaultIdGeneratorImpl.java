package com.suixingpay.takin.id;

import org.apache.commons.lang3.StringUtils;

import com.suixingpay.takin.id.util.FormatUtil;

/**
 * 
 * @author renjinhao
 *
 */
public class DefaultIdGeneratorImpl implements IdGenerator {

	private String serverId;
	private SequenceManager sequenceManager;

	DefaultIdGeneratorImpl(SequenceManager sequenceManager, ServerIdProvider serverIdProvider) {
		this.serverId = serverIdProvider.get();
		this.sequenceManager = sequenceManager;
	}

	public String generate(String namespace) throws IdGenerateException {
		return generate(namespace, null);
	}

	@Override
	public String generate(String namespace, String bizMark) throws IdGenerateException {
		StringBuilder sb = new StringBuilder();
		sb.append(FormatUtil.getDateString());
		sb.append(serverId);
		sb.append(namespace);
		if (StringUtils.isNotBlank(bizMark))
			sb.append(bizMark);
		try {
			sb.append(sequenceManager.getNextSequence(namespace));
		} catch (Exception e) {
			throw new IdGenerateException(e);
		}
		return sb.toString();
	}

}
