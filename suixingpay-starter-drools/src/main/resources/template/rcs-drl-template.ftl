package ${rule.kieBasePackage}

import com.suixingpay.riskengine.message.ContextMessage
import com.suixingpay.riskengine.service.params.RuleParamManageService
<#list rule.imports as importStr>
import ${importStr}
</#list>

global RuleParamManageService ruleParamManageService
<#list rule.imports as importStr>
<#assign className=importStr?substring(importStr?last_index_of(".")+1)>
global ${className} ${className?uncap_first}
</#list>

rule ${rule.ruleCode}
lock-on-active true
when
	//filter
	msg : ContextMessage(
		<#if rule.filterSubRules?size gt 0><#list rule.filterSubRules as filterSubRule>
		<#if filterSubRule_index gt 0>&&  <#else>	</#if>${filterSubRule.express}
		</#list></#if>
	)
	<#if rule.subGroups?size gt 0>
	//groups
	&& 
	(
		<#list rule.subGroups as group>
		<#if group_index gt 0>||</#if>
		//group
		(
			<#if group.functionSubRules?size gt 0>
			//function
			eval(
					ruleParamManageService.getRuleParam("${rule.ruleCode}", msg)
				<#list group.functionSubRules as functionSubRule>
				&&  <#if functionSubRule.standard.format=="01">Integer.parseInt(</#if>${functionSubRule.className?uncap_first}.${functionSubRule.methodName}(msg, "${rule.ruleCode}", "${functionSubRule.elementCode}", <#list functionSubRule.params as param><#if param_index gt 0>, </#if>msg.getTempItem("${param.value}")</#list>)<#if functionSubRule.standard.format=="01">)</#if><#if functionSubRule.standard.symbol?contains("contains")>.contains(msg.getTempItem("${functionSubRule.standard.value}"))<#elseif functionSubRule.standard.format=="02">.equals(msg.getTempItem("${functionSubRule.standard.value}"))<#else> ${functionSubRule.standard.symbol} Integer.parseInt(msg.getTempItem("${functionSubRule.standard.value}").toString())</#if><#if functionSubRule.standard.symbol?contains("!")&&(functionSubRule.standard.symbol?contains("contains")||functionSubRule.standard.format=="02")> == false</#if>
				</#list>
			)
			</#if>
		)
		</#list>
	)</#if>
then
	msg.addRspItem("resCode", "RCSE_${rule.ruleCode}"); 
    msg.addRspItem("resMsg", "当前交易因风险原因被阻断");
end
	