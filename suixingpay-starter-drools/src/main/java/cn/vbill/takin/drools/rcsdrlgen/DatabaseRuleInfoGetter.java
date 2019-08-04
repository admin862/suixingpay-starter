package cn.vbill.takin.drools.rcsdrlgen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.vbill.takin.drools.rcsdrlgen.model.Param;
import cn.vbill.takin.drools.rcsdrlgen.model.Rule;
import cn.vbill.takin.drools.rcsdrlgen.model.Standard;
import cn.vbill.takin.drools.rcsdrlgen.model.SubRuleGroup;
import cn.vbill.takin.drools.rcsdrlgen.model.subrule.FunctionSubRule;
import cn.vbill.takin.drools.rcsdrlgen.model.subrule.FilterSubRule;
import lombok.Data;

/**
 * 根据风控数据库设计，读取数据封装为Rule
 * 
 * @author renjinhao
 *
 */
public class DatabaseRuleInfoGetter implements RuleInfoGetter {

	private static final String GET_RULE_UUID = "select uuid, package_path from rcse.T_RCSE_RULE_CONF where rule_code=?";
	private static final String GET_GROUP_OF_RULE = "select rule_group from rcse.T_RCSE_RULE_CONF_ELEMENT where rule_uuid=? and element_type='02' group by rule_group";
	private static final String GET_ELEMENTS_OF_GROUP = "select a.uuid, a.element_code, a.element_type, b.class_path, b.class_name, b.method_name from rcse.T_RCSE_RULE_CONF_ELEMENT a left join rcse.T_RCSE_RULE_ELEMENT b on a.element_uuid=b.uuid where a.rule_uuid=? and a.rule_group=?";
	private static final String GET_FILTER_ELEMENTS = "select uuid, element_type, class_path, class_name, method_name from rcse.T_RCSE_RULE_CONF_ELEMENT where rule_uuid=? and element_type='05'";
	private static final String GET_PARAMS_OF_ELEMENT = "select param_type, param_format, param_no, param_val, order_seq, logic_symbol, handle_type from rcse.T_RCSE_RULE_CONF_ELEMENT_PARAM where conf_element_uuid=?";

	private static final String PARAM_TYPE_STANDARD = "01";
	private static final String PARAM_TYPE_FUNCTION_PARAM = "02";

	private DataSource dataSource;

	public DatabaseRuleInfoGetter(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Rule get(String ruleCode) throws Exception {
		return execute(c -> getRule(c, ruleCode));
	}

	private Rule getRule(Connection c, String ruleCode) throws SQLException {
		Map<String, String> uuidAndPackagePath = getRuleCodeUuid(c, ruleCode);
		String ruleUuid = uuidAndPackagePath.get("uuid");
		String packagePath = uuidAndPackagePath.get("package_path");
		Rule rule = new Rule();
		rule.setRuleCode(ruleCode);
		rule.setKieBasePackage(packagePath);
		rule.setSubGroups(new ArrayList<>());
		List<RuleElement> filterElements = getFilterElementsOfARule(c, ruleUuid);
		List<FilterSubRule> filterRules = new ArrayList<>();
		rule.setFilterSubRules(filterRules);
		for (RuleElement element : filterElements) {
			List<ElementParam> params = getParamsOfAElement(c, element.getUuid());
			for (ElementParam param : params) {
				FilterSubRule sr = new FilterSubRule();
				sr.setParams(new ArrayList<>());
				sr.setHandle(param.getHandle());
				Param p = new Param();
				p.setValue(param.getParam_no());
				sr.getParams().add(p);
				Standard standard = new Standard();
				standard.setSymbol(param.getLogic_symbol());
				standard.setValue(param.getParam_val());
				standard.setFormat(param.getParam_format());
				sr.setStandard(standard);
				filterRules.add(sr);
			}
		}

		List<String> list = getGroupsOfARule(c, ruleUuid);
		for (String group : list) {
			SubRuleGroup elementGroup = new SubRuleGroup();
			elementGroup.setFunctionSubRules(new ArrayList<>());
			List<RuleElement> elements = getElementsOfAGroup(c, ruleUuid, group);
			for (RuleElement element : elements) {
				FunctionSubRule sr = new FunctionSubRule();
				sr.setElementCode(element.getElement_code());
				sr.setClassPath(element.getClass_path());
				sr.setClassName(element.getClass_name());
				sr.setMethodName(element.getMethod_name());
				sr.setParams(new ArrayList<>());
				List<ElementParam> params = getParamsOfAElement(c, element.getUuid());
				for (ElementParam param : params) {
					if (param.getParam_type().equals(PARAM_TYPE_STANDARD)) {
						Standard standard = new Standard();
						standard.setValue(param.getParam_no());
						standard.setSymbol(param.getLogic_symbol());
						standard.setFormat(param.getParam_format());
						sr.setStandard(standard);
					} else if (param.getParam_type().equals(PARAM_TYPE_FUNCTION_PARAM)) {
						Param p = new Param();
						p.setValue(param.getParam_no());
						if (param.getOrder_seq() != null)
							p.setOrder(Integer.valueOf(param.getOrder_seq()));
						sr.getParams().add(p);
					}
				}
				elementGroup.getFunctionSubRules().add(sr);
			}
			rule.getSubGroups().add(elementGroup);
		}
		return rule;
	}

	private Map<String, String> getRuleCodeUuid(Connection c, String ruleCode) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(GET_RULE_UUID);
			ps.setString(1, ruleCode);
			rs = ps.executeQuery();
			rs.next();
			Map<String, String> result = new HashMap<>(2);
			result.put("uuid", rs.getString("uuid"));
			result.put("package_path", rs.getString("package_path"));
			return result;
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

				}
		}

	}

	private List<String> getGroupsOfARule(Connection c, String rule_uuid) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(GET_GROUP_OF_RULE);
			ps.setString(1, rule_uuid);
			rs = ps.executeQuery();
			List<String> groups = new ArrayList<>();
			while (rs.next()) {
				groups.add(rs.getString(1));
			}
			return groups;
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {

				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

				}
		}
	}

	private List<RuleElement> getElementsOfAGroup(Connection c, String rule_uuid, String group) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(GET_ELEMENTS_OF_GROUP);
			ps.setString(1, rule_uuid);
			ps.setString(2, group);
			rs = ps.executeQuery();
			List<RuleElement> elements = new ArrayList<>();
			while (rs.next()) {
				RuleElement element = new RuleElement();
				element.setUuid(rs.getString("uuid"));
				element.setElement_type(rs.getString("element_type"));
				element.setElement_code(rs.getString("element_code"));
				element.setClass_path(rs.getString("class_path"));
				element.setClass_name(rs.getString("class_name"));
				element.setMethod_name(rs.getString("method_name"));
				elements.add(element);
			}
			return elements;
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {

				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

				}
		}
	}

	private List<RuleElement> getFilterElementsOfARule(Connection c, String rule_uuid) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(GET_FILTER_ELEMENTS);
			ps.setString(1, rule_uuid);
			rs = ps.executeQuery();
			List<RuleElement> elements = new ArrayList<>();
			while (rs.next()) {
				RuleElement element = new RuleElement();
				element.setUuid(rs.getString("uuid"));
				element.setElement_type(rs.getString("element_type"));
				element.setClass_path(rs.getString("class_path"));
				element.setClass_name(rs.getString("class_name"));
				element.setMethod_name(rs.getString("method_name"));
				elements.add(element);
			}
			return elements;
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {

				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

				}
		}
	}

	private List<ElementParam> getParamsOfAElement(Connection c, String element_uuid) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(GET_PARAMS_OF_ELEMENT);
			ps.setString(1, element_uuid);
			rs = ps.executeQuery();
			List<ElementParam> params = new ArrayList<>();
			while (rs.next()) {
				ElementParam param = new ElementParam();
				param.setLogic_symbol(rs.getString("logic_symbol"));
				param.setOrder_seq(rs.getString("order_seq"));
				param.setParam_format(rs.getString("param_format"));
				param.setParam_type(rs.getString("param_type"));
				param.setParam_no(rs.getString("param_no"));
				param.setParam_val(rs.getString("param_val"));
				param.setHandle(rs.getString("handle_type"));
				params.add(param);
			}
			return params;
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {

				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

				}
		}
	}

	private <T extends Object> T execute(JdbcCallback<T> callback) throws SQLException {
		Connection c = null;
		try {
			c = dataSource.getConnection();
			return callback.doInConnection(c);
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException e) {

				}
		}
	}

	private static interface JdbcCallback<T> {
		T doInConnection(Connection c) throws SQLException;
	}

	@Data
	private static class RuleElement {
		private String uuid;
		private String element_type;
		private String element_code;
		private String class_path;
		private String class_name;
		private String method_name;
	}

	@Data
	private static class ElementParam {
		private String param_type;
		private String param_format;
		private String param_no;
		private String order_seq;
		private String logic_symbol;
		private String param_val;
		private String handle;
	}

}
