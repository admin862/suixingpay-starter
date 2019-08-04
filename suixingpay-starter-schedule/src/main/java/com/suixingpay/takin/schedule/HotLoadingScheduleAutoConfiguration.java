package com.suixingpay.takin.schedule;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import com.suixingpay.takin.schedule.dbbased.DbBasedSchedulesInfoProvider;
import com.suixingpay.takin.schedule.dbbased.DefaultTableInfoProvider;
import com.suixingpay.takin.schedule.dbbased.TableInfoProvider;

/**
 * 
 * @author renjinhao
 *
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnBean(HotLoadingScheduleMarkerConfiguration.Marker.class)
@EnableConfigurationProperties(HotLoadingScheduleProperties.class)
public class HotLoadingScheduleAutoConfiguration {

	@Autowired
	private HotLoadingScheduleProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public TableInfoProvider tableInfoProvider() {
		return new DefaultTableInfoProvider();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(DataSource.class)
	public HotLoadingSchedulesInfoProvider schedulesInfo(DataSource dataSource, TableInfoProvider tableInfoProvider) {
		return new DbBasedSchedulesInfoProvider(dataSource, tableInfoProvider);
	}

	@Bean
	public ReloadableTaskRegistry reloadableTaskRegistry() {
		return new ReloadableTaskRegistry();
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public HotLoadingScheduleBeanPostProcessor dbBasedScheduledBeanPostProcessor(
			HotLoadingSchedulesInfoProvider schedulesInfo, ReloadableTaskRegistry reloadableTaskRegistry) {
		return new HotLoadingScheduleBeanPostProcessor(schedulesInfo, reloadableTaskRegistry, properties);
	}
}
