<?xml version="1.0" encoding="UTF-8"?>
<kmodule xmlns="http://jboss.org/kie/6.0.0/kmodule">
	<#list packages as p>
	<kbase name="${p}_base" packages="${p}">
		<ksession name="${p}"/>
	</kbase>
	</#list>
</kmodule>