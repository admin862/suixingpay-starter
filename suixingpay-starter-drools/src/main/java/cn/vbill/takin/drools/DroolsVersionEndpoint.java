package cn.vbill.takin.drools;

import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

/**
 * 
 * @author renjinhao
 *
 */
public class DroolsVersionEndpoint extends AbstractEndpoint<String> {

	private static final String KJAR_VERSION = "kjar";

	@Autowired
	private KieContainer kieContainer;

	public DroolsVersionEndpoint() {
		super(KJAR_VERSION, false);
	}

	@Override
	public String invoke() {
		if (kieContainer == null)
			return "KieContainer is null";
		ReleaseId releaseId = kieContainer.getReleaseId();
		StringBuilder sb = new StringBuilder();
		sb.append("/" + releaseId.getGroupId().replaceAll("\\.", "/") + "/");
		sb.append(releaseId.getArtifactId());
		sb.append("-");
		sb.append(releaseId.getVersion());
		sb.append(".jar");
		return sb.toString();
	}

}
