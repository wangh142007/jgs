package com.wh.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.wh.pojo.bo.ShopcartBO;
import com.wh.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: latte
 * @description:
 * @author: wh
 * @create: 2020-01-13 14:08
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/shopcart")
@Api(value = "购物车接口", tags = {"购物车接口相关的的api"})
public class ShopcatController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存


        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        //TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品数据

        return IMOOCJSONResult.ok();
    }
}
