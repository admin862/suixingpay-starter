package cn.vbill.takin.drools.rcsdrlgen;

import lombok.Data;

/**
 * 
 * @author renjinhao
 *
 */
@Data
public class DrlFile {
	private String fileName;
	private String filePath;
	private byte[] fileContent;
}
