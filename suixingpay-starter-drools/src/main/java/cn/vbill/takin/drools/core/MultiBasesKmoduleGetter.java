package cn.vbill.takin.drools.core;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;

/**
 * 
 * @author renjinhao
 *
 */
public class MultiBasesKmoduleGetter implements KmoduleGetter {

	private Configuration configuration;
	@Setter
	private List<String> packages;
	private static final String templateName = "kmodule-template.ftl";
	private static final String templatePath = "/template/";

	public MultiBasesKmoduleGetter(Configuration configuration) {
		this.configuration = configuration;
		configuration.setClassForTemplateLoading(this.getClass(), templatePath);
	}

	@Override
	public String get() throws Exception {
		Template template = configuration.getTemplate(templateName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter bw = new OutputStreamWriter(baos);
		Map<String, Object> dataModel = new HashMap<>(1);
		dataModel.put("packages", packages);
		template.process(dataModel, bw);
		byte[] bytes = baos.toByteArray();
		bw.close();
		return new String(bytes);
	}

}
