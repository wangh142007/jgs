package com.wh.controller;

import cn.hutool.crypto.SecureUtil;
import com.wh.pojo.Users;
import com.wh.pojo.bo.UserBO;
import com.wh.service.UserService;
import com.wh.utils.CookieUtils;
import com.wh.utils.IMOOCJSONResult;
import com.wh.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: latte
 * @description:
 * @author: wh
 * @create: 2020-01-14 17:51
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/passport")
@Api(value = "注册登录", tags = {"用于注册登录的接口"})
public class PassportController {

    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        //1.用户名不为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //2.查找注册用户名是存在的
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //3.请求成功
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO
            ,  HttpServletRequest request
            , HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //1.账号和密码不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.查询用户名是否一致
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //3.密码长度不能少于6为
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }
        //4.判断密码两次是否一致
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }
        //5.实现注册
        Users user = userService.createUser(userBO);
        user = setNullProperty(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO
            , HttpServletRequest request
            , HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //1.账号和密码不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //5.实现登录
        Users user = userService.queryUserForLogin(username, SecureUtil.md5(password));

        if (user == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        user = setNullProperty(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);


        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return IMOOCJSONResult.ok(user);
    }

    private Users setNullProperty(Users user) {
        user.setUpdatedTime(null);
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setBirthday(null);
        return user;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }


}
