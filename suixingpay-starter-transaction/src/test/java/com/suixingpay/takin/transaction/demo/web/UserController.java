package com.suixingpay.takin.transaction.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.transaction.demo.service.UserService;
import com.suixingpay.takin.transaction.demo.to.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:52
 */
@Api(value = "用户接口", description = "用户接口")
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public User add(@Validated @RequestBody User user) {
        user.setCreater("admin");
        user.setUpdater("admin");
        userService.addUser(user);
        return user;
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public void update(@PathVariable("id") Long id, @Validated @RequestBody User user) {
        user.setUpdater("admin");
        userService.updateUser(user);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

}
