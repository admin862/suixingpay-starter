/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月10日 上午10:35:09   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.token.demo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.token.TokenHelper;
import com.suixingpay.takin.token.TokenInfo;
import com.suixingpay.takin.token.annotation.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月10日 上午10:35:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月10日 上午10:35:09
 */
@RestController
@SpringBootApplication
public class TokenDemoApplication {

    @Autowired
    private TokenHelper tokenHelper;

    public static void main(String[] args) {
        SpringApplication.run(TokenDemoApplication.class, args);
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response, String name, String role) {
        Long userId = 100L;
        // 获取角色
        String[] roles = new String[] { role };
        // 处理登录逻辑
        // ... ...
        // 登录成功后, 把登录信息保存到token中

        AuthenticationUser tokenInfo = new AuthenticationUser();
        tokenInfo.setUserId(String.valueOf(userId));
        tokenInfo.setName(name);
        tokenInfo.setRoles(roles);
        return tokenHelper.login(response, tokenInfo);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        boolean rv = tokenHelper.logout(request, response);
        // 一定要判断是否登出成功
        if (rv) {
            return "logout";
        } else {
            return "logout fail";
        }
    }

    @GetMapping("/")
    public String index(AuthenticationUser tokenInfo) {
        if (null == tokenInfo) {
            return "unlogin";
        }
        return "Hello " + tokenInfo.getName() + "!";
    }

    @GetMapping("/index2")
    public String index2(AuthenticationUser tokenInfo) {
        if (null == tokenInfo) {
            return "您还没登录";
        }
        return "Hello " + tokenInfo.getName() + "!";
    }

    @PreAuthorize("admin")
    @GetMapping("/admin")
    public String admin() {
        return "You are admin!";
    }

    @PreAuthorize("supper")
    @GetMapping("supper")
    public String supper() {
        return "You are supper!";
    }

    @Data
    @EqualsAndHashCode(callSuper=true)
    static class AuthenticationUser extends TokenInfo {
        private static final long serialVersionUID = 1L;
        private String name;
        private String[] roles;

        @Override
        public String[] getRoles() {
            return roles;
        }
    }
}
