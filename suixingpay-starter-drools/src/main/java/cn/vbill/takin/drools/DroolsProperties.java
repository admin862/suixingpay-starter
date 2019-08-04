package cn.vbill.takin.drools;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@ConfigurationProperties(prefix = "suixingpay.drools")
@Data
public class DroolsProperties {
	/**
	 * 生成drl模板名的前缀
	 */
	private String system = "rcs";
	/**
	 * kjar的生成路径
	 */
	private String kJarPath = "/home/app/sxpservice/drools/";
	/**
	 * kjar的生成名称
	 */
	private String kJarName = "kJar";
	/**
	 * drl文件的生成路径
	 */
	private String drlPath = "/home/app/sxpservice/drools/drl/";
}
