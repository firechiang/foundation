package net.foundation.mbusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.foundation.mbusiness.domain.BlockchainContract;
import net.foundation.mbusiness.domain.BlockchainContractInfo;

public interface BlockchainContractService extends IService<BlockchainContract> {

    BlockchainContractInfo queryCacheByAddress(String contractAddr);

    BlockchainContract queryByAddress(String contractAddr);

    void removeCache(String contractAddr);

    int saveIgnore(BlockchainContract bc);
}
