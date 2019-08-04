package com.suixingpay.takin.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 将Long数组转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class LongArrayTypeHandler extends BaseTypeHandler<Long[]> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, StringUtils.arrayToDelimitedString(parameter, DELIMITER));
    }

    public static Long[] stringToLongArray(String str) {
        Long[] res = null;
        String[] array = StringUtils.tokenizeToStringArray(str, DELIMITER);
        if (null != array && array.length > 0) {
            res = new Long[array.length];
            for (int i = 0; i < array.length; i++) {
                String tmp = array[i];
                if (null != tmp && tmp.length() > 0) {
                    res[i] = Long.valueOf(tmp);
                }
            }
        }
        return res;
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToLongArray(str);
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToLongArray(str);
    }

    @Override
    public Long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToLongArray(str);
    }

}