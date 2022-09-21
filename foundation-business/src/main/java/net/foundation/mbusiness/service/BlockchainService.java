package net.foundation.mbusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.foundation.mbusiness.domain.Blockchain;
import net.foundation.mbusiness.domain.BlockchainInfo;

public interface BlockchainService extends IService<Blockchain> {

    BlockchainInfo queryCacheByChainId(Integer chainId);

}
