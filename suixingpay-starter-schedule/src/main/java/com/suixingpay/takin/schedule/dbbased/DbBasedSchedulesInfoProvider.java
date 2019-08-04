package com.suixingpay.takin.schedule.dbbased;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.suixingpay.takin.schedule.HotLoadingSchedulesInfoProvider;
import com.suixingpay.takin.util.net.NetUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author renjinhao
 *
 */
@Slf4j
public class DbBasedSchedulesInfoProvider implements HotLoadingSchedulesInfoProvider, EnvironmentAware {

	private static final String SQL_TEMPLATE_1 = "SELECT %c FROM %t";
	private static final String SQL_TEMPLATE_2 = "SELECT %c1 FROM %t WHERE %c2=?";
	private static final String COMMA = ",";

	private static String SELECT_NAMES;
	private static String SELECT_INITIAL_DELAY;
	private static String SELECT_CRON;
	private static String SELECT_FIXED_DELAY;
	private static String SELECT_FIXED_RATE;
	private static String SELECT_SERVERID;
	private static String SELECT_UPDATETIME;

	private DataSource dataSource;
	private String serverId;
	private Map<String, Long> timestamps = new ConcurrentHashMap<>();

	public DbBasedSchedulesInfoProvider(DataSource dataSource, TableInfoProvider table) {
		this.dataSource = dataSource;
		SELECT_NAMES = fillSql(SQL_TEMPLATE_1, table.getJobNameColumn(), null, null, table.getTableName());
		SELECT_INITIAL_DELAY = fillSql(SQL_TEMPLATE_2, null, table.getInitialDelayColumn(), table.getJobNameColumn(),
				table.getTableName());
		SELECT_CRON = fillSql(SQL_TEMPLATE_2, null, table.getCronColumn(), table.getJobNameColumn(),
				table.getTableName());
		SELECT_FIXED_DELAY = fillSql(SQL_TEMPLATE_2, null, table.getFixedDelayColumn(), table.getJobNameColumn(),
				table.getTableName());
		SELECT_FIXED_RATE = fillSql(SQL_TEMPLATE_2, null, table.getFixedRateColumn(), table.getJobNameColumn(),
				table.getTableName());
		SELECT_SERVERID = fillSql(SQL_TEMPLATE_2, null, table.getServerIdColumn(), table.getJobNameColumn(),
				table.getTableName());
		SELECT_UPDATETIME = fillSql(SQL_TEMPLATE_2, null, table.getUpdateTimeColumn(), table.getJobNameColumn(),
				table.getTableName());
	}

	private String fillSql(String template, String c, String c1, String c2, String t) {
		String sql = null;
		sql = template.replaceAll("%t", t);
		if (c != null)
			sql = sql.replaceAll("%c", c);
		if (c1 != null)
			sql = sql.replaceAll("%c1", c1);
		if (c2 != null)
			sql = sql.replaceAll("%c2", c2);
		return sql;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.serverId = NetUtils.getLocalHost() + ":" + environment.resolvePlaceholders("${server.port:8080}");
	}

	@Override
	public List<String> getScheduleNames() {
		return execute(new JdbcCallback<List<String>>() {

			@Override
			public List<String> doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_NAMES);
					rs = ps.executeQuery();
					List<String> result = new ArrayList<>();
					while (rs.next()) {
						result.add(rs.getString(1));
					}
					return result;
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
	}

	@Override
	public String getInitialdelaystring(String scheduleName) {
		return execute(new JdbcCallback<String>() {

			@Override
			public String doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_INITIAL_DELAY);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
	}

	@Override
	public String getCron(String scheduleName) {
		return execute(new JdbcCallback<String>() {

			@Override
			public String doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_CRON);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
	}

	@Override
	public String getFixeddelaystring(String scheduleName) {
		return execute(new JdbcCallback<String>() {

			@Override
			public String doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_FIXED_DELAY);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
	}

	@Override
	public String getFixedratestring(String scheduleName) {
		return execute(new JdbcCallback<String>() {

			@Override
			public String doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_FIXED_RATE);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
	}

	@Override
	public boolean shouldRun(String scheduleName) {
		String serverid = execute(new JdbcCallback<String>() {

			@Override
			public String doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_SERVERID);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
		String[] serverids = serverid.split(COMMA);
		for (String id : serverids) {
			if (id.equals(serverId))
				return true;
		}
		return false;
	}

	@Override
	public boolean isDirty(String scheduleName) {
		Timestamp ts = execute(new JdbcCallback<Timestamp>() {

			@Override
			public Timestamp doInConnection(Connection c) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = c.prepareStatement(SELECT_UPDATETIME);
					ps.setString(1, scheduleName);
					rs = ps.executeQuery();
					rs.next();
					return rs.getTimestamp(1);
				} catch (SQLException e) {
					return null;
				} finally {
					try {
						if (ps != null)
							ps.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
					}
				}
			}

		});
		if (ts == null)
			return false;
		Long newTimestamp = ts.getTime();
		Long timestamp = timestamps.get(scheduleName);
		if (timestamp == null || newTimestamp > timestamp) {
			timestamps.put(scheduleName, newTimestamp);
			return true;
		}
		return false;
	}

	private <T> T execute(JdbcCallback<T> callback) {
		Connection c = null;
		try {
			c = dataSource.getConnection();
			return callback.doInConnection(c);
		} catch (SQLException e) {
			log.error("查询定时任务配置异常");
			return null;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException e) {
				}
		}
	}

	private static interface JdbcCallback<T> {
		T doInConnection(Connection c);
	}

}
