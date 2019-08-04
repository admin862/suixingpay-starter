package cn.vbill.takin.drools.rcsdrlgen.model.subrule;

import cn.vbill.takin.drools.rcsdrlgen.model.SubRule;
import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class FunctionSubRule extends SubRule {
	private String classPath;
	private String className;
	private String methodName;
}
