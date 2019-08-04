package com.suixingpay.takin.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.suixingpay.takin.util.json.JsonUtil;

/**
 * 数据库中存json格式
 * 
 * @author jiayu.qiu
 * @param <E>
 */
public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final TypeReference<T> reference;

    public AbstractJsonTypeHandler(TypeReference<T> reference) {
        this.reference = reference;
        if (null == reference) {
            throw new IllegalArgumentException("reference is null");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.objectToJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return JsonUtil.jsonToObject(json, reference);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return JsonUtil.jsonToObject(json, reference);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return JsonUtil.jsonToObject(json, reference);
    }

}