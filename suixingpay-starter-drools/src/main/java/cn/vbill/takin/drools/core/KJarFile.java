package cn.vbill.takin.drools.core;

import java.io.File;

import org.kie.api.builder.ReleaseId;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
@AllArgsConstructor
public class KJarFile {
	private File file;
	private ReleaseId releaseId;
}
