package net.foundation.mapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.foundation.mbusiness.domain.BlockchainContractInfo;
import net.foundation.mbusiness.service.BlockchainContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试Api")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BlockchainContractService blockchainContractService;

    @GetMapping("/get")
    @ApiOperation(value = "测试Get请求")
    public String get(@RequestParam("param") String param) {
        BlockchainContractInfo blockchainContractInfo = blockchainContractService.queryCacheByAddress(param);
        System.err.println(blockchainContractInfo);
        return "测试数据";
    }

}
