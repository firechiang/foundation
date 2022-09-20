package net.foundation.mbusiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("f_blockchain_contract")
public class BlockchainContract {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 简称
     */
    private String symbol;
    /**
     * 精度
     */
    private Integer decimals;
    /**
     *
     */
    private String abi;
    /**
     * 创建时间
     */
    private Date createTime;
}
