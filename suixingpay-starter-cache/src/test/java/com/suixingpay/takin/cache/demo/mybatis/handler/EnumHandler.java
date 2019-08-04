/**
 * 
 */
package com.suixingpay.takin.cache.demo.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:34:50
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:34:50
 */
@Alias("EnumHandler")
public class EnumHandler<E extends Enum<E> & Identifiable<K>, K> extends BaseTypeHandler<E> {

    // private final Class<E> type;

    private final EnumSet<E> enumSet;

    public EnumHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        // this.type = type;
        this.enumSet = EnumSet.allOf(type);
        if (enumSet == null || enumSet.size() <= 0) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            K id = parameter.getId();
            if (id instanceof Integer || id instanceof Short || id instanceof Character || id instanceof Byte) {
                ps.setInt(i, (Integer) id);
            } else if (id instanceof String) {
                ps.setString(i, (String) id);
            } else if (id instanceof Boolean) {
                ps.setBoolean(i, (Boolean) id);
            } else if (id instanceof Long) {
                ps.setLong(i, (Long) id);
            } else if (id instanceof Double) {
                ps.setDouble(i, (Double) id);
            } else if (id instanceof Float) {
                ps.setFloat(i, (Float) id);
            } else {
                throw new RuntimeException("unsupported [id] type of enum");
            }
        } else {
            ps.setObject(i, parameter.getId(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return toEnum(s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return toEnum(s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return toEnum(s);
    }

    private E toEnum(String id) {
        for (E e : enumSet) {
            K k = e.getId();
            if (k != null) {
                if (k.toString().equals(id)) {
                    return e;
                }
            }
        }
        return null;
    }
}