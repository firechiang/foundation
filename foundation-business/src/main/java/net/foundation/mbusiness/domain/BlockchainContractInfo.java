package net.foundation.mbusiness.domain;

import lombok.Data;
import net.foundation.mcrypto.decoder.AbiDecoder;

@Data
public class BlockchainContractInfo {

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
    private String ctype;
    /**
     * 精度
     */
    private Integer decimals;

    private AbiDecoder abiDecoder;

    private boolean loadDisk;
}
