package com.suixingpay.takin.schedule.dbbased;

/**
 * 
 * @author renjinhao
 *
 */
public class DefaultTableInfoProvider implements TableInfoProvider {

	private static final String JOBNAME = "JOBNAME";
	private static final String INITIALDELAYSTRING = "INITIALDELAYSTRING";
	private static final String CRON = "CRON";
	private static final String FIXEDDELAYSTRING = "FIXEDDELAYSTRING";
	private static final String FIXEDRATESTRING = "FIXEDRATESTRING";
	private static final String SERVERID = "SERVERID";
	private static final String UPDATETIME = "UPDATETIME";
	private static final String TABLE_NAME = "T_SXP_SCHEDULE";

	@Override
	public String getJobNameColumn() {
		return JOBNAME;
	}

	@Override
	public String getInitialDelayColumn() {
		return INITIALDELAYSTRING;
	}

	@Override
	public String getCronColumn() {
		return CRON;
	}

	@Override
	public String getFixedDelayColumn() {
		return FIXEDDELAYSTRING;
	}

	@Override
	public String getFixedRateColumn() {
		return FIXEDRATESTRING;
	}

	@Override
	public String getServerIdColumn() {
		return SERVERID;
	}

	@Override
	public String getUpdateTimeColumn() {
		return UPDATETIME;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

}
