package net.foundation.mbusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.foundation.mbusiness.domain.BlockchainContractType;
import net.foundation.mbusiness.domain.BlockchainContractTypeInfo;

public interface BlockchainContractTypeService extends IService<BlockchainContractType> {

    BlockchainContractTypeInfo queryCacheByName(String name);

    BlockchainContractType queryByName(String name);

}
