package net.foundation.mbusiness.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BlockchainInfo {

    private Integer id;
    /**
     * 链ID
     */
    private Integer chainId;
    /**
     * 接口地址
     */
    private String rpcUrl;
    /**
     * 区块浏览器地址
     */
    private String  explorerUrl;
    /**
     * 最新区块高度
     */
    private Long lastHeight;
    /**
     * 精度
     */
    private BigDecimal decimalsPow;
}
