package cn.vbill.takin.drools.core;

import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildKieModule;
import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.setDefaultsforEmptyKieModule;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.drools.compiler.kie.builder.impl.InternalKieContainer;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.ResultsImpl;
import org.drools.compiler.kie.builder.impl.ZipKieModule;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据给定ReleaseId读取并加载jar包，ReleaseId可能是给定的(refresh(String
 * version))，也可能是调用{@code KJarGenerator}生成的最新jar包
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class LocalKjarRefresher {

	private InternalKieContainer kieContainer;

	private KJarGenerator kJarGenerator;

	private ReleaseId lastReleaseId;

	public LocalKjarRefresher(InternalKieContainer kieContainer, KJarGenerator kJarGenerator) {
		this.kieContainer = kieContainer;
		this.kJarGenerator = kJarGenerator;
		this.lastReleaseId = kieContainer.getContainerReleaseId();
	}

	public synchronized void refresh(String version) {
		ReleaseId releaseId = kJarGenerator.getReleaseId(version);
		String path = "/" + releaseId.getGroupId().replaceAll("\\.", "/") + "/";
		File file = new File(
				path + releaseId.getArtifactId() + "-" + releaseId.getVersion() + KJarGenerator.JAR_SUBFIX);
		KJarFile kjar = new KJarFile(file, releaseId);
		updateKieModule(lastReleaseId, kjar);
		lastReleaseId = kjar.getReleaseId();
		log.info("refresh drools rule based on database, version=" + lastReleaseId.getVersion());
	}

	public synchronized void refresh() throws Exception {
		KJarFile kJarFile = kJarGenerator.generateKJar();
		updateKieModule(lastReleaseId, kJarFile);
		lastReleaseId = kJarFile.getReleaseId();
		log.info("refresh drools rule based on database, version=" + lastReleaseId.getVersion());
	}

	private void updateKieModule(ReleaseId oldReleaseId, KJarFile newKJar) {
		ReleaseId newReleaseId = newKJar.getReleaseId();
		ZipKieModule kieModule = createZipKieModule(newReleaseId, newKJar.getFile());
		if (kieModule != null) {
			ResultsImpl messages = build(kieModule);
			if (messages.filterMessages(Message.Level.ERROR).isEmpty()) {
				kieContainer.updateDependencyToVersion(lastReleaseId, newReleaseId);
			}
		}
	}

	private ResultsImpl build(InternalKieModule kieModule) {
		ResultsImpl messages = new ResultsImpl();
		buildKieModule(kieModule, messages);
		return messages;
	}

	private static ZipKieModule createZipKieModule(ReleaseId releaseId, File jar) {
		KieModuleModel kieModuleModel = getKieModuleModelFromJar(jar);
		return kieModuleModel != null ? new ZipKieModule(releaseId, kieModuleModel, jar) : null;
	}

	private static KieModuleModel getKieModuleModelFromJar(File jar) {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(jar);
			ZipEntry zipEntry = zipFile.getEntry(KieModuleModelImpl.KMODULE_JAR_PATH);
			KieModuleModel kieModuleModel = KieModuleModelImpl.fromXML(zipFile.getInputStream(zipEntry));
			setDefaultsforEmptyKieModule(kieModuleModel);
			return kieModuleModel;
		} catch (Exception e) {
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
