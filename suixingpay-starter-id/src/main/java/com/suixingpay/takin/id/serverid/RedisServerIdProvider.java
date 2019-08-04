package com.suixingpay.takin.id.serverid;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.suixingpay.takin.id.ServerIdProvider;
import com.suixingpay.takin.id.util.FormatUtil;
import com.suixingpay.takin.redis.IRedisOperater;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class RedisServerIdProvider implements ServerIdProvider, EnvironmentAware {

	private static final String ID_SERVERID_GENERATOR_KEY = "suixingpay_id_serverid_generator";
	private static final String ID_PREFIX = "suixingpay_id_serverid_";
	private static final Long MAX_SERVER_ID = 16 * 16 * 16 * 16 * 16 * 16L;
	private static final int MAX_ID_LENGTH = 6;
	private static final String SEPERATOR = ":";

	private String serverId;
	private Environment env;
	private IRedisOperater redis;

	public RedisServerIdProvider(IRedisOperater redis) {
		this.redis = redis;
	}

	@PostConstruct
	public void init() {
		String appName = env.getProperty("spring.application.name");
		String appIndex = env.getProperty("spring.application.index");
		if (StringUtils.isBlank(appName) || appName.contains(SEPERATOR))
			throw new ServerIdProviderException("spring.application.name参数不存在或格式非法");
		if (StringUtils.isBlank(appIndex) || appIndex.contains(SEPERATOR))
			throw new ServerIdProviderException("spring.application.index参数不存在或格式非法");
		String key = ID_PREFIX + appName + SEPERATOR + appIndex;
		serverId = (String) redis.get(key);
		if (serverId == null) {
			long serverIdLong = redis.incr(ID_SERVERID_GENERATOR_KEY);
			if (serverIdLong > MAX_SERVER_ID)
				throw new ServerIdProviderException("serverId溢出");
			String hex = Long.toHexString(serverIdLong);
			this.serverId = FormatUtil.formatSeq(hex, MAX_ID_LENGTH);
			redis.set(key, serverId);
		}
		if (log.isDebugEnabled())
			log.debug("current serverId=" + serverId);
	}

	@Override
	public String get() {
		return serverId;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}
