/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月24日 下午10:24:40   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.mybatis.page.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.suixingpay.takin.mybatis.page.dialect.impl.Db2Dialect;
import com.suixingpay.takin.mybatis.page.dialect.impl.HsqldbDialect;
import com.suixingpay.takin.mybatis.page.dialect.impl.InformixDialect;
import com.suixingpay.takin.mybatis.page.dialect.impl.MySqlDialect;
import com.suixingpay.takin.mybatis.page.dialect.impl.OracleDialect;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月24日 下午10:24:40
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月24日 下午10:24:40
 */
@Slf4j
public class DialectUtil {
    private static final Map<String, Class<?>> DIALECT_ALIAS_MAP = new HashMap<String, Class<?>>(20);
    private static final Map<DataSource, Dialect> DATASOURCE_DIALECT_MAP = new ConcurrentHashMap<>(10);
    // 多数据源时，获取jdbcurl后是否关闭数据源
    private static boolean closeConn = true;

    static {
        // 注册别名
        DIALECT_ALIAS_MAP.put("hsqldb", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("h2", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("postgresql", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("phoenix", HsqldbDialect.class);

        DIALECT_ALIAS_MAP.put("mysql", MySqlDialect.class);
        DIALECT_ALIAS_MAP.put("mariadb", MySqlDialect.class);
        DIALECT_ALIAS_MAP.put("sqlite", MySqlDialect.class);

        DIALECT_ALIAS_MAP.put("oracle", OracleDialect.class);
        DIALECT_ALIAS_MAP.put("db2", Db2Dialect.class);
        DIALECT_ALIAS_MAP.put("informix", InformixDialect.class);
        // 解决 informix-sqli #129，仍然保留上面的
        DIALECT_ALIAS_MAP.put("informix-sqli", InformixDialect.class);

        // DIALECT_ALIAS_MAP.put("sqlserver", SqlServerDialect.class);
        // DIALECT_ALIAS_MAP.put("sqlserver2012", SqlServer2012Dialect.class);

        // DIALECT_ALIAS_MAP.put("derby", SqlServer2012Dialect.class);
    }

    /**
     * 获取url
     *
     * @param dataSource
     * @return
     */
    private static String getDatabaseProductName(DataSource dataSource) {
        Connection conn = null;
        String dbname = null;
        String jdbcUrl = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dbMetaData = conn.getMetaData();
            jdbcUrl = dbMetaData.getURL();
            log.trace("JDBC Version:{}.{}", dbMetaData.getJDBCMajorVersion(), dbMetaData.getJDBCMinorVersion());
            log.trace("Driver Version:{}.{}", dbMetaData.getDriverMajorVersion(), dbMetaData.getDriverMinorVersion());
            // 数据库产品名称,用它来区分数据库类型
            dbname = dbMetaData.getDatabaseProductName().toLowerCase();
            log.trace("Database Product Name:{}", dbname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    if (closeConn) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
        if (null == dbname || dbname.length() == 0) {
            log.error("database product name is empty from jdbcUrl:{}", jdbcUrl);
            throw new RuntimeException("无法自动获取DatabaseProductName!");
        }
        return dbname;
    }

    private static Class<?> fromDatabaseProductName(String dbname) {
        Iterator<Map.Entry<String, Class<?>>> iterator = DIALECT_ALIAS_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Class<?>> item = iterator.next();
            if (item.getKey().equals(dbname)) {
                return item.getValue();
            }
        }
        throw new RuntimeException("无法自动获取数据库类型!DatabaseProductName:" + dbname);
    }

    /**
     * 根据 DataSource 获取数据库方言
     *
     * @param ms
     * @return
     * @throws Exception
     */
    public static Dialect getDialect(DataSource dataSource) throws Exception {
        Dialect dialect = DATASOURCE_DIALECT_MAP.get(dataSource);
        if (null == dialect) {
            synchronized (dataSource) {
                dialect = DATASOURCE_DIALECT_MAP.get(dataSource);
                if (null == dialect) {
                    String dbname = getDatabaseProductName(dataSource);
                    Class<?> clazz = fromDatabaseProductName(dbname);
                    dialect = (Dialect) clazz.newInstance();
                    DATASOURCE_DIALECT_MAP.put(dataSource, dialect);
                }
            }
        }
        return dialect;
    }
}
