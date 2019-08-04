package cn.vbill.takin.drools.rcsdrlgen;

import cn.vbill.takin.drools.rcsdrlgen.model.Rule;

/**
 * 根据给定ruleCode获取Rule对象默认实现为{@code DatabaseRuleInfoGetter}
 * @author renjinhao
 *
 */
public interface RuleInfoGetter {
	
	public Rule get(String ruleCode) throws Exception;
}
