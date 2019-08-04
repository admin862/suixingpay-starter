package com.suixingpay.takin.mybatis.typehandler;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.suixingpay.takin.data.enums.BaseEnum;

/**
 * @author jiayu.qiu
 * @param <E>
 */
public abstract class BaseEnumTypeHandler<E extends Enum<? extends BaseEnum>> extends BaseTypeHandler<E> {

    private Map<Integer, E> map = new HashMap<Integer, E>();

    public BaseEnumTypeHandler() {
        @SuppressWarnings("unchecked")
        Class<E> entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        E[] enums = entityClass.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(entityClass.getSimpleName() + " does not represent an enum type.");
        }
        for (E e : enums) {
            BaseEnum BaseEnum = (BaseEnum) e;
            map.put(BaseEnum.getCode(), e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, ((BaseEnum) parameter).getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return map.get(id);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        if (rs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return map.get(id);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        if (cs.wasNull()) {// wasNull，必须放到get方法后面使用
            return null;
        }
        return map.get(id);
    }

}