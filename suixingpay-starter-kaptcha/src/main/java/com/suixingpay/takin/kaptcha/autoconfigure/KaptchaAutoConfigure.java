/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2018年1月3日 下午1:16:22   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptcha.autoconfigure;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.suixingpay.takin.redis.IRedisOperater;
import com.suixingpay.takin.kaptcha.KaptchaHelper;
import com.suixingpay.takin.kaptcha.repository.KaptchaRepository;
import com.suixingpay.takin.kaptcha.repository.RedisKaptchaRepository;

/**
 * kaptcha 自动配置
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月3日 下午1:16:22
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月3日 下午1:16:22
 */
@Configuration
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaAutoConfigure {

    @Autowired
    private KaptchaProperties properties;

    @Bean
    @ConditionalOnMissingBean({ Producer.class })
    public Producer sxfCaptchaProducer() {
        Properties props = new Properties();
        // 是否有边框, 默认为true 可选yes 或者 no
        props.put("kaptcha.border", properties.getBorder());
        // 边框颜色, 默认为Color.BLACK
        props.put("kaptcha.border.color", properties.getBorderColor());
        // 边框粗细度 默认为1
        props.put("kaptcha.border.thickness", properties.getBorderThickness());
        // 验证码图片的宽度 默认200
        props.put("kaptcha.image.width", properties.getImageWidth());
        // 验证码图片的高度 默认50
        props.put("kaptcha.image.height", properties.getImageHeight());
        // 验证码文本字符长度 默认为5
        props.put("kaptcha.textproducer.char.length", properties.getTextproducerCharLength());
        // 验证码文本字体样式 默认为Arial,Courier
        props.put("kaptcha.textproducer.font.names", properties.getTextproducerFontNames());
        // 验证码文本字符大小 默认为40
        props.put("kaptcha.textproducer.font.size", properties.getTextproducerFontSize());
        // 验证码文本字符颜色 默认为Color.BLACK
        props.put("kaptcha.textproducer.font.color", properties.getTextproducerFontColor());
        // 验证码文本字符内容范围 默认为abcde2345678gfynmnpwx
        props.put("kaptcha.textproducer.char.string", properties.getTextproducerCharString());
        // 验证码生成器 默认为DefaultKaptcha
        props.put("kaptcha.producer.impl", properties.getProducerImpl());
        // 验证码文本生成器 默认为DefaultTextCreator
        props.put("kaptcha.textproducer.impl", properties.getTextproducerImpl());
        // 验证码文本字符间距 默认为2
        props.put("kaptcha.textproducer.char.space", properties.getTextproducerCharSpace());
        // 验证码噪点生成对象 默认为DefaultNoise
        props.put("kaptcha.noise.impl", properties.getNoiseImpl());
        // 验证码噪点颜色 默认为Color.BLACK
        props.put("kaptcha.noise.color", properties.getNoiseColor());
        // 验证码样式引擎 默认为WaterRipple
        props.put("kaptcha.obscurificator.impl", properties.getObscurificatorImpl());
        // 验证码文本字符渲染 默认为DefaultWordRenderer
        props.put("kaptcha.word.impl", properties.getWordImpl());
        // 验证码背景生成器 默认为DefaultBackground
        props.put("kaptcha.background.impl", properties.getBackgroundImpl());
        // 验证码背景颜色渐进 默认为Color.LIGHT_GRAY
        props.put("kaptcha.background.clear.from", properties.getBackgroundClearFrom());
        // 验证码背景颜色渐进 默认为Color.WHITE
        props.put("kaptcha.background.clear.to", properties.getBackgroundClearTo());

        Config config = new Config(props);
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    @Bean
    @ConditionalOnMissingBean({ KaptchaRepository.class })
    public KaptchaRepository sxfKaptchaRepository(IRedisOperater redisOperater) {
        return new RedisKaptchaRepository(redisOperater);
    }

    @Bean
    @ConditionalOnMissingBean({ KaptchaHelper.class })
    public KaptchaHelper sxfKaptchaHelper(Producer sxfCaptchaProducer, KaptchaRepository sxfKaptchaRepository) {
        return new KaptchaHelper(properties, sxfCaptchaProducer, sxfKaptchaRepository);
    }
}
