package com.suixingpay.takin.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 将Long List转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, StringUtils.collectionToDelimitedString(parameter, DELIMITER));
    }

    public static List<Long> stringToList(String string) {
        String[] array = StringUtils.tokenizeToStringArray(string, DELIMITER);
        List<Long> res = null;
        if (null != array && array.length > 0) {
            res = new ArrayList<>(array.length);
            for (String item : array) {
                Long val = null;
                if (null != item && item.length() > 0) {
                    val = Long.valueOf(item);
                }
                res.add(val);
            }
        }
        return res;
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToList(str);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToList(str);
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToList(str);
    }

}