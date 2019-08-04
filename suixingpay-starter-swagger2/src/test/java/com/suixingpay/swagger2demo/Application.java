/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: qiujiayu[qiu_jy@suixingpay.com] 
 * @date: 2017年11月10日 上午10:35:09   
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.swagger2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.exception.BaseException;
import com.suixingpay.takin.exception.SystemException;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月10日 上午10:35:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月10日 上午10:35:09
 */
@Slf4j
@RestController
@SpringBootApplication
public class Application {

    /**
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    */
    @GetMapping("/index")
    public String index() {
        log.debug("index");
        return "Hello wolrd!";
    }
    
    @GetMapping("/error-test")
    public String error(@RequestParam(required=false) String t) throws BaseException{
        log.debug("error-test");
        if(null != t && t.length()>0) {
            throw new SystemException("异常测试");
        }
        log.debug("这是一条debug信息");
        return "Hello wolrd!";
    }
}
