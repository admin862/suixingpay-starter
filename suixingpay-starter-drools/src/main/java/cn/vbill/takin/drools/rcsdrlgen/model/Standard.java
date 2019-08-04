package cn.vbill.takin.drools.rcsdrlgen.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class Standard {

	private static Map<String, String> mapping = new HashMap<>(7);
	static {
		mapping.put("01", ">");
		mapping.put("02", ">=");
		mapping.put("03", "==");
		mapping.put("04", "<");
		mapping.put("05", "<=");
		mapping.put("06", "&&");
		mapping.put("07", "||");
	}

	private String symbol;
	private String value;
	private String format;

	public String getSymbol() {
		return mapping.get(symbol);
	}

}
