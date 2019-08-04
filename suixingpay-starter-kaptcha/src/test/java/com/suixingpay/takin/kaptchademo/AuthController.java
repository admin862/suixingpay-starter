/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月14日 下午4:52:42   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.kaptchademo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.kaptcha.KaptchaHelper;

import io.swagger.annotations.Api;
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
@Api(description = "登录鉴权接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public KaptchaHelper kaptchaHelper;

    @PostMapping("/login")
    public Boolean login(//
            @ApiParam(name = "name", value = "登录名", required = true) //
            @Valid @NotNull(message = "登录名不能为空") //
            @RequestParam("name") String name, //

            @ApiParam(name = "pass", value = "密码", required = true) //
            @Valid @NotNull(message = "密码不能为空") //
            @RequestParam("pass") String pass, //

            @ApiParam(name = "codekey", value = "验证码key", required = true) //
            @Valid @NotNull(message = "验证码key不能为空") //
            @RequestParam("codekey") String codekey, //

            @ApiParam(name = "code", value = "验证码", required = true) //
            @Valid @NotBlank(message = "验证码不能为空") //
            @RequestParam("code") String code) {
        return kaptchaHelper.check(codekey, code);
    }
}
