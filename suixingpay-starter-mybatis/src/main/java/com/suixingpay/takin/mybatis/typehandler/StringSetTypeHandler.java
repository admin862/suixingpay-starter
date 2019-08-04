package com.suixingpay.takin.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 将字符串Set转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class StringSetTypeHandler extends BaseTypeHandler<Set<String>> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, StringUtils.collectionToDelimitedString(parameter, DELIMITER));
    }

    public static Set<String> stringToSet(String string) {
        String[] array = StringUtils.tokenizeToStringArray(string, DELIMITER);
        Set<String> res = null;
        if (null != array && array.length > 0) {
            res = new LinkedHashSet<>(array.length);
            for (String item : array) {
                res.add(item);
            }
        }
        return res;
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

}