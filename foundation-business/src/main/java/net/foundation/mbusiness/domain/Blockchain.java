package net.foundation.mbusiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 区块链信息数据
 */
@Data
@TableName("f_blockchain")
public class Blockchain {

    @TableId(type= IdType.AUTO)
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
     * 创建时间
     */
    private Date createTime;
}
