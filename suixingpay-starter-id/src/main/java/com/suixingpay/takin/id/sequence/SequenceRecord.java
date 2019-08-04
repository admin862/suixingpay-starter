package com.suixingpay.takin.id.sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suixingpay.takin.id.IdProperties;
import com.suixingpay.takin.id.IdProperties.IdConfig;
import com.suixingpay.takin.util.json.JsonUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author renjinhao
 *
 */
@Data
@Slf4j
public class SequenceRecord {

	private Map<String, NamespaceRecord> namespaceRecords;
	@JsonIgnore
	private IdProperties properties;

	public SequenceRecord() {
	}

	public SequenceRecord(IdProperties properties) throws IOException {
		this.properties = properties;
		String content = load();
		SequenceRecord record = JsonUtil.jsonToObject(content, SequenceRecord.class);
		if (record != null)
			this.namespaceRecords = record.getNamespaceRecords();
		else
			this.namespaceRecords = new HashMap<>();
		loadNextChunk(namespaceRecords.keySet());
		loadNextChunk(namespaceRecords.keySet());
	}

	private IdConfig getConfig(String namespace) {
		IdConfig config = properties.getConfigs().get(namespace);
		if (config == null) {
			config = new IdConfig();
			properties.getConfigs().put(namespace, config);
		}
		return config;
	}

	private NamespaceRecord addNamespaceRecord(String namespace) throws IOException {
		NamespaceRecord nr = createNamespaceRecord(namespace);
		if (log.isDebugEnabled()) {
			log.debug("addNewRecord:" + this.toString());
		}
		record();
		return nr;
	}

	private NamespaceRecord createNamespaceRecord(String namespace) {
		IdConfig config = getConfig(namespace);
		NamespaceRecord nr = new NamespaceRecord();
		nr.setNamespace(namespace);
		nr.setFirstPosition(BigDecimal.ZERO);
		nr.setFirstChunkSize(config.getChunkSize());
		nr.setSecondPosition(nr.getFirstPosition().add(BigDecimal.valueOf(nr.getFirstChunkSize())));
		nr.setSecondChunkSize(config.getChunkSize());
		namespaceRecords.put(namespace, nr);
		return nr;
	}

	public NamespaceRecord getNamespaceRecord(String namespace) throws IOException {
		NamespaceRecord nr = namespaceRecords.get(namespace);
		if (nr == null) {
			nr = addNamespaceRecord(namespace);
		}
		return nr;
	}

	public void loadNextChunk(Collection<String> namespaces) throws IOException {
		for (String namespace : namespaces) {
			updateNameRecords(namespace);
		}
		if (log.isDebugEnabled()) {
			log.debug("loadNextChunk:" + this.toString());
		}
		record();
	}

	private void updateNameRecords(String namespace) throws IOException {
		IdConfig config = getConfig(namespace);
		NamespaceRecord nr = namespaceRecords.get(namespace);
		if (nr == null) {
			createNamespaceRecord(namespace);
			return;
		}
		nr.setFirstPosition(nr.getSecondPosition());
		nr.setFirstChunkSize(nr.getSecondChunkSize());
		nr.setSecondPosition(nr.getFirstPosition().add(BigDecimal.valueOf(nr.getFirstChunkSize())));
		nr.setSecondChunkSize(config.getChunkSize());
		resetIfNeccesary(namespace);
	}

	private void resetIfNeccesary(String namespace) {
		NamespaceRecord nr = namespaceRecords.get(namespace);
		BigDecimal capacity = nr.getSecondPosition().add(BigDecimal.valueOf(nr.getSecondChunkSize())).subtract(BigDecimal.ONE);
		IdConfig config = getConfig(namespace);
		if (capacity.toString().length() > config.getSeqLength()) {
			nr.setSecondPosition(BigDecimal.ZERO);
			nr.setSecondChunkSize(config.getChunkSize());
		}
	}

	private void record() throws IOException {
		String seqFilePath = properties.getSeqFilePath();
		String content = JsonUtil.objectToJson(this);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(seqFilePath)));
			writer.write(content);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	private String load() throws IOException {
		String seqFilePath = properties.getSeqFilePath();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(seqFilePath)));
			String temp = null;
			StringBuilder sb = new StringBuilder();
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			return sb.toString();
		} finally {
			if (br != null)
				br.close();
		}
	}

	@Data
	static class NamespaceRecord {
		private String namespace;
		private BigDecimal firstPosition;
		private Integer firstChunkSize;
		private BigDecimal secondPosition;
		private Integer secondChunkSize;

	}
}
