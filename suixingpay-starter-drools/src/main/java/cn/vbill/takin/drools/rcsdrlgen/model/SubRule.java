package cn.vbill.takin.drools.rcsdrlgen.model;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class SubRule {
	protected String elementCode;
	protected List<Param> params;
	protected Standard standard;
}
