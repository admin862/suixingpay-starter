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
 * 将字Integer Set 转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class IntegerSetTypeHandler extends BaseTypeHandler<Set<Integer>> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<Integer> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, StringUtils.collectionToDelimitedString(parameter, DELIMITER));
    }

    public static Set<Integer> stringToSet(String string) {
        String[] array = StringUtils.tokenizeToStringArray(string, DELIMITER);
        Set<Integer> res = null;
        if (null != array && array.length > 0) {
            res = new LinkedHashSet<>(array.length);
            for (String item : array) {
                Integer val = null;
                if (null != item && item.length() > 0) {
                    val = Integer.valueOf(item);
                }
                res.add(val);
            }
        }
        return res;
    }

    @Override
    public Set<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

    @Override
    public Set<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

    @Override
    public Set<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return stringToSet(str);
    }

}