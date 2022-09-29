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
     * Logo
     */
    private String logo;
    /**
     * 合约类型
     */
    private String ctype;
    /**
     * 精度
     */
    private Integer decimals;
    /**
     * 合约编码
     */
    private String abi;
    /**
     * 官网
     */
    private String officialSite;
    /**
     * 创建时间
     */
    private Date createTime;
}
