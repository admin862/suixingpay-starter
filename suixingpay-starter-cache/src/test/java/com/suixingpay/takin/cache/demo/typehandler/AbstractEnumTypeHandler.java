/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年8月15日 上午11:45:41   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.cache.demo.typehandler;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.suixingpay.takin.cache.demo.type.AbstractEnum;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月15日 上午11:45:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月15日 上午11:45:41
 */
@MappedJdbcTypes({ JdbcType.SMALLINT })
@MappedTypes({ AbstractEnum.class })
public abstract class AbstractEnumTypeHandler<E extends Enum<? extends AbstractEnum>> extends BaseTypeHandler<E> {

    private Class<E> type;
    private final E[] enums;

    @SuppressWarnings("unchecked")
    public AbstractEnumTypeHandler() {
        this.type = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    private E getById(int id) {
        for (E e : enums) {
            AbstractEnum itype = (AbstractEnum) e;
            if (id == itype.getId().intValue()) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        AbstractEnum p = (AbstractEnum) parameter;
        ps.setInt(i, p.getId());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                return getById(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by id value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                return getById(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by id value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            try {
                return getById(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by id value.", ex);
            }
        }
    }

}
