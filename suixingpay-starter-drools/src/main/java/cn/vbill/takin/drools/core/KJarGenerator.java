package cn.vbill.takin.drools.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.builder.ReleaseId;

import cn.vbill.takin.drools.rcsdrlgen.DrlFile;
import lombok.AllArgsConstructor;

/**
 * 读取drlPath下的drl文件，并生成kjar
 * 
 * @author renjinhao
 *
 */
@AllArgsConstructor
public class KJarGenerator {

	private static final String KMODULE_PATH = "META-INF/kmodule.xml";

	private static final String DEFAULT_VERSION = "1.0";
	public static final String JAR_SUBFIX = ".jar";
	public static final String RULE_FILE_SUBFIX = ".drl";

	private String kJarPath;
	private String kJarName;
	private String drlPath;
	private KmoduleGetter kmoduleGetter;

	@PostConstruct
	public void checkPath() {
		if (!kJarPath.endsWith("/"))
			kJarPath += "/";
		if (!drlPath.endsWith("/"))
			drlPath += "/";
	}

	public ReleaseId getReleaseId(String version) {
		return new ReleaseIdImpl(kJarPath.substring(1, kJarPath.length() - 1).replaceAll("/", "."), kJarName, version);
	}

	public KJarFile generateKJar() throws Exception {
		File drlDir = new File(drlPath);
		List<File> drlFiles = new ArrayList<>();
		getDrlFiles(drlDir, drlFiles);

		List<DrlFile> drls = new ArrayList<>();
		for (File file : drlFiles) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath())));
				StringBuilder sb = new StringBuilder();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sb.append(temp).append("\n");
				}
				DrlFile drl = new DrlFile();
				drl.setFileContent(sb.toString().getBytes());
				String path = file.getPath();
				path = path.replaceAll(file.getName(), "");
				drl.setFilePath(path.substring(drlPath.length(), path.length() - 1));
				drl.setFileName(file.getName());
				drls.add(drl);
			} finally {
				if (br != null)
					br.close();
			}
		}
		return generateKJar(drls);
	}

	private void getDrlFiles(File dir, List<File> files) {
		File[] subFiles = dir.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile() && subFile.getName().endsWith(RULE_FILE_SUBFIX))
				files.add(subFile);
			else if (subFile.isDirectory()) {
				getDrlFiles(subFile, files);
			}
		}
	}

	private KJarFile generateKJar(List<DrlFile> drls) throws Exception {
		FileOutputStream fos = null;
		try {
			String version = DEFAULT_VERSION + "-"
					+ LocalDateTime.now().toString().replaceAll("[-T:]", "").substring(0, 14);
			File file = new File(kJarPath + kJarName + "-" + version + JAR_SUBFIX);
			fos = new FileOutputStream(file);
			Manifest manifest = new Manifest();
			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			JarOutputStream jarOs = new JarOutputStream(fos, manifest);
			List<String> packages = drls.stream().map(d -> d.getFilePath()).map(p -> p.replaceAll("[\\\\/]", "."))
					.distinct().collect(Collectors.toList());
			genKmodel(jarOs, packages);
			for (DrlFile drl : drls) {
				genDrl(jarOs, drl);
			}
			jarOs.close();
			ReleaseId releaseId = new ReleaseIdImpl(kJarPath.substring(1, kJarPath.length() - 1).replaceAll("/", "."),
					kJarName, version);
			return new KJarFile(file, releaseId);
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	private void genKmodel(JarOutputStream jarOs, List<String> packages) throws Exception {
		if (kmoduleGetter instanceof MultiBasesKmoduleGetter)
			((MultiBasesKmoduleGetter) kmoduleGetter).setPackages(packages);
		JarEntry kmoduleEntry = new JarEntry(KMODULE_PATH);
		jarOs.putNextEntry(kmoduleEntry);
		jarOs.write(kmoduleGetter.get().getBytes());
		jarOs.flush();
		jarOs.closeEntry();
	}

	private void genDrl(JarOutputStream jarOs, DrlFile drl) throws IOException {
		JarEntry drlEntry = new JarEntry(drl.getFilePath() + "/" + drl.getFileName());
		jarOs.putNextEntry(drlEntry);
		jarOs.write(drl.getFileContent());
		jarOs.flush();
		jarOs.closeEntry();
	}

}
