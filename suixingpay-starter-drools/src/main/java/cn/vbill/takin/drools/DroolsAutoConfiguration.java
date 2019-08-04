package cn.vbill.takin.drools;

import java.io.File;

import javax.annotation.PostConstruct;

import org.drools.compiler.kie.builder.impl.InternalKieContainer;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.vbill.takin.drools.core.KJarGenerator;
import cn.vbill.takin.drools.core.KieServicesFactory;
import cn.vbill.takin.drools.core.KmoduleGetter;
import cn.vbill.takin.drools.core.LocalKjarRefresher;
import cn.vbill.takin.drools.core.MultiBasesKmoduleGetter;

/**
 * 
 * @author renjinhao
 *
 */
@Configuration
@EnableConfigurationProperties(DroolsProperties.class)
public class DroolsAutoConfiguration {

	@Autowired
	private DroolsProperties droolsProperties;

	@PostConstruct
	public void check() {
		File f = new File(droolsProperties.getKJarPath());
		if (!f.exists())
			f.mkdirs();
		f = new File(droolsProperties.getDrlPath());
		if (!f.exists())
			f.mkdirs();
	}

	@Bean
	public KmoduleGetter kmoduleGetter(freemarker.template.Configuration configuration) {
		return new MultiBasesKmoduleGetter(configuration);
	}

	@Bean
	public KJarGenerator kJarGenerator(KmoduleGetter kmoduleGetter) {
		return new KJarGenerator(droolsProperties.getKJarPath(), droolsProperties.getKJarName(),
				droolsProperties.getDrlPath(), kmoduleGetter);
	}

	@Bean
	public KieServices kieServices() {
		return KieServicesFactory.get();
	}

	@Bean
	public KieContainer kieContainer(KieServices kieServices, KJarGenerator kJarGenerator) throws Exception {
		KieContainer kieContainer = kieServices.newKieContainer(kJarGenerator.generateKJar().getReleaseId());
		return kieContainer;
	}

	@Bean
	public LocalKjarRefresher localKjarRefresher(KieContainer kieContainer, KJarGenerator kJarGenerator) {
		return new LocalKjarRefresher((InternalKieContainer) kieContainer, kJarGenerator);
	}

	@Bean
	public DroolsVersionEndpoint droolsVersionEndpoint() {
		return new DroolsVersionEndpoint();
	}
}
