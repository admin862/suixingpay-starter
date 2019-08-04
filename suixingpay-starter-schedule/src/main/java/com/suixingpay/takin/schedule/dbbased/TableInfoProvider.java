package com.suixingpay.takin.schedule.dbbased;

/**
 * 
 * @author renjinhao
 *
 */
public interface TableInfoProvider {

	String getTableName();

	String getJobNameColumn();

	String getInitialDelayColumn();

	String getCronColumn();

	String getFixedDelayColumn();

	String getFixedRateColumn();

	String getServerIdColumn();

	String getUpdateTimeColumn();
}
