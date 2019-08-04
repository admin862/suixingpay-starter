package com.suixingpay.takin.kaptcha.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 验证码参数设置
 * 
 * @author jiayu.qiu
 */
@Data
@ConfigurationProperties(prefix = "suixingpay.kaptcha")
public class KaptchaProperties {

    /**
     * 验证码在缓存中的存活时长
     */
    private Integer expire = 120;
    /**
     * 是否有边框, 默认为no 可选yes 或者 no
     */
    private String border = "no";
    /**
     * 边框颜色, 默认为Color.BLACK
     */
    private String borderColor = "black";
    /**
     * 边框粗细度 默认为1
     */
    private Integer borderThickness = 1;

    /**
     * 验证码图片的宽度 默认200
     */
    private Integer imageWidth = 200;

    /**
     * 验证码图片的高度 默认50
     */
    private Integer imageHeight = 50;

    /**
     * 验证码文本字符位数 默认为5
     */
    private Integer textproducerCharLength = 5;
    /**
     * 验证码文本字体样式,默认为Arial,Courier
     */
    private String textproducerFontNames = "Arial,Courier";

    /**
     * 验证码文本字符大小 默认为40
     */
    private Integer textproducerFontSize = 40;
    /**
     * 验证码文本字符颜色 默认为Color.BLUE
     */
    private String textproducerFontColor = "BLUE";
    /**
     * 验证码文本字符内容范围 默认为aAbBcCdDeEfFgG2345678hHjJkKmMLnNpPrRsStTuUvVwWxXyY
     */
    private String textproducerCharString = "aAbBcCdDeEfFgG2345678hHjJkKmMLnNpPrRsStTuUvVwWxXyY";
    /**
     * 验证码文本生成器 默认为DefaultTextCreator
     */
    private String textproducerImpl = "";

    /**
     * 验证码文本字符间距 默认为2
     */
    private Integer textproducerCharSpace = 2;

    /**
     * 验证码生成器 默认为DefaultKaptcha
     */
    private String producerImpl = "";
    /**
     * 验证码噪点生成对象 默认为DefaultNoise
     */
    private String noiseImpl = "";

    /**
     * 验证码噪点颜色 默认为Color.BLACK
     */
    private String noiseColor = "BLACK";

    /**
     * 验证码样式引擎 默认为WaterRipple
     */
    private String obscurificatorImpl = "";
    /**
     * 验证码文本字符渲染 默认为DefaultWordRenderer
     */
    private String wordImpl = "";

    /**
     * 验证码背景生成器 默认为DefaultBackground
     */
    private String backgroundImpl = "";

    /**
     * 验证码背景颜色渐进色 默认为Color.LIGHT_GRAY
     */
    private String backgroundClearFrom = "LIGHT_GRAY";

    /**
     * 验证码背景颜色渐出色 默认为Color.WHITE
     */
    private String backgroundClearTo = "WHITE";
}
