package net.foundation.mbusiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 区块链智能合约数据
 */
@Data
@TableName("f_blockchain_contract")
public class BlockchainContract {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 合约地址
     */
    private String addr;
    /**
     * 名称
     */
    private String name;
    /**
     * 合约类型
     */
    private Integer ctype;
    /**
     * 简称
     */
    private String symbol;
    /**
     * 精度
     */
    private Integer decimals;
    /**
     * 合约编码
     */
    private String abi;
    /**
     * 创建时间
     */
    private Date createTime;
}
