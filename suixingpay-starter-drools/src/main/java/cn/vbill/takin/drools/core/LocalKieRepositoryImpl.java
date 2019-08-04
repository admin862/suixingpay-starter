package cn.vbill.takin.drools.core;

import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildKieModule;
import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.setDefaultsforEmptyKieModule;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.ResultsImpl;
import org.drools.compiler.kie.builder.impl.ZipKieModule;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 代理KieRepositoryImpl，扩展本地读取
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class LocalKieRepositoryImpl implements KieRepository {

	public static final KieRepository INSTANCE = new LocalKieRepositoryImpl();

	private KieRepository internalKieRepository = KieServices.Factory.get().getRepository();

	public KieModule getKieModule(ReleaseId releaseId) {
		KieModule kieModul = internalKieRepository.getKieModule(releaseId);
		if (kieModul != null)
			return kieModul;
		String path = "/" + releaseId.getGroupId().replaceAll("\\.", "/") + "/";
		File kjar = new File(
				path + releaseId.getArtifactId() + "-" + releaseId.getVersion() + KJarGenerator.JAR_SUBFIX);
		if (!kjar.exists())
			return null;
		ZipKieModule kieModule = createZipKieModule(releaseId, kjar);
		if (kieModule != null) {
			build(kieModule);
		}
		return kieModule;
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
			log.error("build kmodule fail", e);
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

	private ResultsImpl build(InternalKieModule kieModule) {
		ResultsImpl messages = new ResultsImpl();
		buildKieModule(kieModule, messages);
		return messages;
	}

	@Override
	public ReleaseId getDefaultReleaseId() {
		return internalKieRepository.getDefaultReleaseId();
	}

	@Override
	public void addKieModule(KieModule kModule) {
		internalKieRepository.addKieModule(kModule);
	}

	@Override
	public KieModule addKieModule(Resource resource, Resource... dependencies) {
		return internalKieRepository.addKieModule(resource, dependencies);
	}

	@Override
	public KieModule removeKieModule(ReleaseId releaseId) {
		return internalKieRepository.removeKieModule(releaseId);
	}
}
