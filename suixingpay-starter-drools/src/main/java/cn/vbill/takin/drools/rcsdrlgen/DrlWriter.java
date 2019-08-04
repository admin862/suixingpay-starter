package cn.vbill.takin.drools.rcsdrlgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.annotation.PostConstruct;

import cn.vbill.takin.drools.rcsdrlgen.model.Rule;
import lombok.AllArgsConstructor;

/**
 * 
 * @author renjinhao
 *
 */
@AllArgsConstructor
public class DrlWriter {

	private RuleInfoGetter ruleInfoGetter;
	private DrlGenerator drlGenerator;
	private String drlPath;

	@PostConstruct
	public void checkDrlPath() {
		if (!drlPath.endsWith("/"))
			drlPath += "/";
	}

	public void write(String ruleCode) throws Exception {
		Rule rule = ruleInfoGetter.get(ruleCode);
		DrlFile drl = drlGenerator.write(rule);
		File dir = new File(drlPath + drl.getFilePath());
		if (!dir.exists())
			dir.mkdirs();
		String file = drlPath + drl.getFilePath() + drl.getFileName();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			bw.write(new String(drl.getFileContent()));
		} finally {
			if (bw != null)
				bw.close();
		}
	}
}
