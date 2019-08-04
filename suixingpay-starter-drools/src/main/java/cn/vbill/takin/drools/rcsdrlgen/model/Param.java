package cn.vbill.takin.drools.rcsdrlgen.model;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class Param implements Comparable<Param> {
	private String value;
	private int order = -1;

	@Override
	public int compareTo(Param p) {
		return order - p.getOrder();
	}
}
