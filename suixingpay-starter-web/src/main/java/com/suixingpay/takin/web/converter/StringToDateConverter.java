package com.suixingpay.takin.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 字符串转日期的转换器
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 上午9:43:45
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 上午9:43:45
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final String CN_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String CN_DATE_TIME_FORMAT1 = "yyyy-MM-dd HH:mm";
    private static final String CN_DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final String DATE_TIME_FORMAT1 = "yyyyMMddHHmm";
    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String DATE_TIME_FORMAT2 = "yyMMddHHmmss";
    private static final String DATE_FORMAT2 = "yyMMdd";
    
    private static final String TIME_FORMAT = "HH:mm:ss";

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = source.trim();
        try {
            int len = source.length();
            int ind1 = source.indexOf("-");
            int ind2 = source.indexOf(":");
            SimpleDateFormat formatter = null;
            if (len == CN_DATE_TIME_FORMAT.length() && ind1 != -1 && ind2 != -1) {
                formatter = new SimpleDateFormat(CN_DATE_TIME_FORMAT);
            } else if (len == CN_DATE_TIME_FORMAT1.length() && ind1 != -1 && ind2 != -1) {
                formatter = new SimpleDateFormat(CN_DATE_TIME_FORMAT1);
            } else if (len == CN_DATE_FORMAT.length() && ind1 != -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(CN_DATE_FORMAT);
            } else if (len == DATE_TIME_FORMAT.length() && ind1 == -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            } else if (len == DATE_TIME_FORMAT1.length() && ind1 == -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(DATE_TIME_FORMAT1);
            } else if (len == DATE_FORMAT.length() && ind1 == -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(DATE_FORMAT);
            } else if (len == DATE_TIME_FORMAT2.length() && ind1 == -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(DATE_TIME_FORMAT2);
            } else if (len == DATE_FORMAT2.length() && ind1 == -1 && ind2 == -1) {
                formatter = new SimpleDateFormat(DATE_FORMAT2);
            } else if (len == TIME_FORMAT.length() && ind1 == -1 && ind2 != -1) {
                formatter = new SimpleDateFormat(TIME_FORMAT);
            }
            if (null != formatter) {
                Date dtDate = formatter.parse(source);
                return dtDate;
            } else if (ind1 == -1 && ind2 == -1 && source.matches("^\\d+$")) {
                Long lDate = new Long(source);
                return new Date(lDate);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", source));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", source));
    }
}