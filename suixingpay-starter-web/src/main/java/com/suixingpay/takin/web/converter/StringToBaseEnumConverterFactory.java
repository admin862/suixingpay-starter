package com.suixingpay.takin.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.suixingpay.takin.data.enums.BaseEnum;
import com.suixingpay.takin.data.util.EnumUtil;

/**
 * 字符串转继承了BaseEnum 的枚举
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年2月1日 上午9:43:45
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年2月1日 上午9:43:45
 */
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToBaseEnum(targetType);
    }

    private class StringToBaseEnum<T extends Enum<? extends BaseEnum>> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToBaseEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (null == source || source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            try {
                Integer code = Integer.parseInt(source);
                return EnumUtil.getByCode(enumType, code);
            } catch (Throwable t) {

            }
            return EnumUtil.getByName(enumType, source);
        }
    }
}