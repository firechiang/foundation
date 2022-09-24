package net.foundation.mbusiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 区块链智能合约类型
 */
@Data
@TableName("f_blockchain_contract_type")
public class BlockchainContractType {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 合约编码
     */
    private String abi;
    /**
     * 创建时间
     */
    private Date createTime;

}
