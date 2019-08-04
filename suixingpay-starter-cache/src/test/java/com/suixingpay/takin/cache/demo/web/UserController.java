package com.suixingpay.takin.cache.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.cache.demo.service.UserService;
import com.suixingpay.takin.cache.demo.to.User;

import io.swagger.annotations.ApiOperation;

/**
 * TODO
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:52
 */
// @Api(value="用户接口", description="用户接口")
@RestController
// @RequestMapping(value="/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> list() {
        return null;
    }

    @ApiOperation(value = "根据ID获取用户信息", notes = "根据ID获取用户信息")
    @GetMapping("/{id}")
    public User list(@PathVariable Integer id) {
        System.out.println("getuser" + id);
        User user = userService.getUserById2(id);
        System.out.println(user);
        return userService.getUserById(id);
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @GetMapping("/update1/{id}")
    public void update1(@PathVariable Integer id) {
        User user = new User();
        user.setId(id);
        userService.updateUser(user);
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @GetMapping("/update2/{id}")
    public void update2(@PathVariable Integer id) {
        User user = new User();
        user.setId(id);
        userService.updateUser2(user);
    }
}
