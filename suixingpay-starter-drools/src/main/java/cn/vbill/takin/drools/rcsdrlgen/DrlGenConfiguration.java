package cn.vbill.takin.drools.rcsdrlgen;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import cn.vbill.takin.drools.DroolsProperties;

/**
 * 
 * @author renjinhao
 *
 */
@EnableConfigurationProperties(DroolsProperties.class)
public class DrlGenConfiguration {

	@Autowired
	private DroolsProperties droolsProperties;

	@PostConstruct
	public void check() {
		File f = new File(droolsProperties.getDrlPath());
		if (!f.exists())
			f.mkdirs();
	}

	@Bean
	public DrlGenerator drlGenerator(freemarker.template.Configuration configuration) throws IOException {
		return new DrlGenerator(configuration, droolsProperties.getSystem());
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(DataSource.class)
	public RuleInfoGetter ruleInfoGetter(DataSource dataSource) {
		return new DatabaseRuleInfoGetter(dataSource);
	}

	@Bean
	public DrlWriter drlWriter(RuleInfoGetter ruleInfoGetter, DrlGenerator drlGenerator) {
		return new DrlWriter(ruleInfoGetter, drlGenerator, droolsProperties.getDrlPath());
	}
}
