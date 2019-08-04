package com.suixingpay.takin.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 将Integer数组转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class IntegerArrayTypeHandler extends BaseTypeHandler<Integer[]> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, StringUtils.arrayToDelimitedString(parameter, DELIMITER));
    }

    public static Integer[] stringToIntegerArray(String str) {
        Integer[] res = null;
        String[] array = StringUtils.tokenizeToStringArray(str, DELIMITER);
        if (null != array && array.length > 0) {
            res = new Integer[array.length];
            for (int i = 0; i < array.length; i++) {
                String tmp = array[i];
                if (null != tmp && tmp.length() > 0) {
                    res[i] = Integer.valueOf(tmp);
                }
            }
        }
        return res;
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToIntegerArray(str);
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToIntegerArray(str);
    }

    @Override
    public Integer[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToIntegerArray(str);
    }

}