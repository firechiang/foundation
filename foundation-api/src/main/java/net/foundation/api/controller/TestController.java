package net.foundation.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试Api")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    @ApiOperation(value = "测试Get请求")
    public String get(){

        return "测试数据";
    }

}
