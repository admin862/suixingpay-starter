package cn.vbill.takin.drools.rcsdrlgen.model.subrule;

import cn.vbill.takin.drools.rcsdrlgen.model.SubRule;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author renjinhao
 *
 */
public class FilterSubRule extends SubRule {

	@Getter
	@Setter
	private String handle;

	public String getExpress() {
		StringBuilder sb = new StringBuilder();
		if ("03".equals(handle)) {
			if ("&&".equals(standard.getSymbol()) || "||".equals(standard.getSymbol())) {
				String[] values = standard.getValue().split(",");
				sb.append("(");
				for (int i = 0; i < values.length; i++) {
					if (i > 0)
						sb.append(" ").append(standard.getSymbol()).append(" ");
					if ("01".equals(standard.getFormat()))
						sb.append("Integer.parseInt(");
					sb.append("msg.getTempItem(\"" + params.get(0).getValue() + "\")");
					if ("01".equals(standard.getFormat()))
						sb.append(")");
					sb.append(" == ");
					if ("02".equals(standard.getFormat()))
						sb.append("\"");
					sb.append(values[i]);
					if ("02".equals(standard.getFormat()))
						sb.append("\"");
				}
				sb.append(")");
			} else {
				if ("01".equals(standard.getFormat()))
					sb.append("Integer.parseInt(");
				sb.append("msg.getTempItem(\"" + params.get(0).getValue() + "\")");
				if ("01".equals(standard.getFormat()))
					sb.append(")");
				sb.append(" ").append(standard.getSymbol()).append(" ");
				if ("02".equals(standard.getFormat()))
					sb.append("\"");
				sb.append(standard.getValue());
				if ("02".equals(standard.getFormat()))
					sb.append("\"");
			}
		} else {
			if ("&&".equals(standard.getSymbol()) || "||".equals(standard.getSymbol())) {
				String[] values = standard.getValue().split(",");
				sb.append("(");
				for (int i = 0; i < values.length; i++) {
					if (i > 0)
						sb.append(" ").append(standard.getSymbol()).append(" ");
					if ("01".equals(standard.getFormat()))
						sb.append("Integer.parseInt(");
					sb.append("msg.walker().req(\"" + params.get(0).getValue() + "\")");
					if ("01".equals(standard.getFormat()))
						sb.append(")");
					sb.append(" == ");
					if ("02".equals(standard.getFormat()))
						sb.append("\"");
					sb.append(values[i]);
					if ("02".equals(standard.getFormat()))
						sb.append("\"");
				}
				sb.append(")");
			} else {
				if ("01".equals(standard.getFormat()))
					sb.append("Integer.parseInt(");
				sb.append("msg.walker().req(\"" + params.get(0).getValue() + "\")");
				if ("01".equals(standard.getFormat()))
					sb.append(")");
				sb.append(" ").append(standard.getSymbol()).append(" ");
				if ("02".equals(standard.getFormat()))
					sb.append("\"");
				sb.append(standard.getValue());
				if ("02".equals(standard.getFormat()))
					sb.append("\"");
			}
		}
		return sb.toString();
	}
}
