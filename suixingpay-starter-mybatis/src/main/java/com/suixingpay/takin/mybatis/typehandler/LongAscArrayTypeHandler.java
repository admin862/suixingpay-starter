package com.suixingpay.takin.mybatis.typehandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 将Long升序数组转成逗号隔开的字符串
 * 
 * @author jiayu.qiu
 */
public class LongAscArrayTypeHandler extends LongArrayTypeHandler {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
            throws SQLException {
        // 进行排序
        Arrays.sort(parameter);
        ps.setString(i, StringUtils.arrayToDelimitedString(parameter, DELIMITER));
    }
}