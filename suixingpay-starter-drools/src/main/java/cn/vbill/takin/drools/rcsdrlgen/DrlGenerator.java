package cn.vbill.takin.drools.rcsdrlgen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import cn.vbill.takin.drools.rcsdrlgen.model.Rule;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 使用freemarker模板将Rule生成为drl文件，默认模板为rcs-drl-template.ftl，其中rcs可以通过suixingpay.drools.system定义，替换模板
 * 
 * @author renjinhao
 *
 */
public class DrlGenerator {

	private static final String templatePath = "/template/";
	private static final String templateName = "-drl-template.ftl";
	private static final String fileNameSubfix = ".drl";

	private Configuration configuration;
	private String system;

	public DrlGenerator(Configuration configuration, String system) throws IOException {
		this.configuration = configuration;
		this.system = system;
		configuration.setClassForTemplateLoading(this.getClass(), templatePath);
	}

	public DrlFile write(Rule rule) throws TemplateException, IOException {
		rule.init();
		Template template = configuration.getTemplate(system + templateName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter bw = new OutputStreamWriter(baos);
		Map<String, Object> dataModel = new HashMap<>(1);
		dataModel.put("rule", rule);
		template.process(dataModel, bw);
		byte[] bytes = baos.toByteArray();
		bw.close();
		DrlFile file = new DrlFile();
		file.setFileContent(bytes);
		file.setFilePath(rule.getKieBasePackage().replaceAll("\\.", "/") + "/");
		file.setFileName(rule.getRuleCode() + fileNameSubfix);
		return file;
	}
}
