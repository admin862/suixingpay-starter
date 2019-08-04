package cn.vbill.takin.drools.rcsdrlgen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.vbill.takin.drools.rcsdrlgen.model.subrule.FilterSubRule;
import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class Rule {

	private String ruleCode;
	private String kieBasePackage;
	private List<FilterSubRule> filterSubRules;
	private List<SubRuleGroup> subGroups;
	private List<String> imports;

	public void init() {
		subGroups.stream().flatMap(g -> g.getFunctionSubRules().stream()).forEach(r -> Collections.sort(r.getParams()));
		imports = new ArrayList<>();
		subGroups.stream().flatMap(g -> g.getFunctionSubRules().stream()).distinct()
				.forEach(r -> imports.add(r.getClassPath() + "." + r.getClassName()));
	}
}
