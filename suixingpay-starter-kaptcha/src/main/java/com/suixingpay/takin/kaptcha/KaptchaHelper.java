/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月14日 下午5:46:20   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Producer;
import com.suixingpay.takin.kaptcha.autoconfigure.KaptchaProperties;
import com.suixingpay.takin.kaptcha.dto.Kaptcha;
import com.suixingpay.takin.kaptcha.repository.KaptchaRepository;

/**
 * google Kaptcha 验证码生成工具类
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午5:46:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午5:46:20
 */
public class KaptchaHelper {

    private static final String IMG_SRC_PREFIX = "data:image/jpeg;base64,";

    private final KaptchaProperties properties;

    private final Producer captchaProducer;

    private final KaptchaRepository kaptchaRepository;

    public KaptchaHelper(KaptchaProperties properties, Producer captchaProducer, KaptchaRepository kaptchaRepository) {
        this.properties = properties;
        this.captchaProducer = captchaProducer;
        this.kaptchaRepository = kaptchaRepository;
    }

    /**
     * 生成验证码，并放到Redis中
     * 
     * @return
     * @throws Exception
     */
    public Kaptcha createImage() throws Exception {
        // 生成验证码文本
        String capText = captchaProducer.createText();
        // 利用生成的字符串构建图片
        BufferedImage bi = captchaProducer.createImage(capText);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", out);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        Kaptcha kaptcha = new Kaptcha();
        kaptcha.setKey(key);
        kaptcha.setBase64Code(IMG_SRC_PREFIX + Base64.getEncoder().encodeToString(out.toByteArray()));
        // 缓存到Redis中
        kaptchaRepository.set(key, capText, properties.getExpire());
        return kaptcha;
    }

    /**
     * 检查验证码是否正确
     * 
     * @param key
     * @param inputCode
     * @return
     */
    public boolean check(String key, String inputCode) {
        if (null == key || key.length() == 0 || null == inputCode || inputCode.length() == 0) {
            return false;
        }
        String code = kaptchaRepository.get(key);
        return inputCode.equalsIgnoreCase(code);
    }
}
