/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月14日 下午4:52:42   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptchademo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.kaptcha.KaptchaHelper;
import com.suixingpay.takin.kaptcha.dto.Kaptcha;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 图片验证码
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午4:52:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午4:52:42
 */
@Validated
@Api(description = "图片验证码接口")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    public KaptchaHelper kaptchaHelper;

    /**
     * 生成验证码
     * 
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "生成验证码", notes = "生成验证码")
    @GetMapping("/gen")
    public Kaptcha getCaptchaCode(HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        return kaptchaHelper.createImage();
    }

    /**
     * 检查验证码是否正确
     * 
     * @param key
     * @param captchaCode
     * @return
     */
    @ApiOperation(value = "检查验证码是否正确", notes = "检查验证码是否正确")
    @GetMapping("/check")
    public Boolean checkCaptchaCode(@ApiParam(name = "key", value = "验证码key", required = true) //
    @Valid @NotNull(message = "验证码key不能为空") //
    @RequestParam("key") String key, //
            @ApiParam(name = "code", value = "验证码", required = true) //
            @Valid @NotBlank(message = "验证码不能为空") //
            @RequestParam("code") String code) {
        return kaptchaHelper.check(key, code);
    }
}
