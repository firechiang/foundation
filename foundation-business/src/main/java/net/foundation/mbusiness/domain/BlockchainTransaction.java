package net.foundation.mbusiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 区块链交易数据
 */
@Data
@TableName("f_blockchain_transaction")
public class BlockchainTransaction {

    @TableId(type= IdType.AUTO)
    private Long id;
    /**
     * 链ID
     */
    private Integer chainId;
    /**
     * 链上交易ID
     */
    private String txHash;
    /**
     * 转出地址
     */
    private String form;
    /**
     * 收款地址
     */
    private String to;
    /**
     * 智能合约地址
     */
    private String contract;
    /**
     * 智能合约函数
     */
    private String method;
    /**
     * 数量
     */
    private BigDecimal amount;
    /**
     * 费用
     */
    private BigDecimal gas;
    /**
     * 创建时间
     */
    private Date createTime;
}
