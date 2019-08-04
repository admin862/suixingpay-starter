package cn.vbill.takin.drools.rcsdrlgen.model;

import java.util.List;

import cn.vbill.takin.drools.rcsdrlgen.model.subrule.FunctionSubRule;
import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class SubRuleGroup {
	private List<FunctionSubRule> functionSubRules;
}
