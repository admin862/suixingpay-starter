package cn.vbill.takin.ratelimit.strategy;

/**
 * 
 * @author renjinhao
 *
 */
public interface Invocation {

	void invoke() throws Exception;

	void fallback() throws Exception;
}
