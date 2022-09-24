package net.foundation.mbusiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.foundation.mbusiness.domain.BlockchainContract;

public interface BlockchainContractMapper extends BaseMapper<BlockchainContract> {

    int insertIgnore(BlockchainContract bc);

}
