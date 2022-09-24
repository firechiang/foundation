package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.foundation.mbusiness.domain.BlockchainContractType;
import net.foundation.mbusiness.domain.BlockchainContractTypeInfo;
import net.foundation.mbusiness.mapper.BlockchainContractTypeMapper;
import net.foundation.mbusiness.service.BlockchainContractTypeService;
import net.foundation.mcrypto.decoder.AbiDecoder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlockchainContractTypeServiceImpl extends ServiceImpl<BlockchainContractTypeMapper, BlockchainContractType> implements BlockchainContractTypeService {

    private final Map<String, BlockchainContractTypeInfo> cache = new ConcurrentHashMap<>();

    @Override
    public BlockchainContractTypeInfo queryCacheByName(String name) {
        BlockchainContractTypeInfo info = this.cache.get(name);
        if(Objects.isNull(info)) {
            BlockchainContractType bct = this.queryByName(name);
            if(Objects.nonNull(bct)) {
                info = new BlockchainContractTypeInfo();
                BeanUtils.copyProperties(bct,info);
                info.setAbiDecoder(AbiDecoder.create(bct.getAbi()));
                this.cache.put(name,info);
            }
        }
        return info;
    }

    @Override
    public BlockchainContractType queryByName(String name) {
        LambdaQueryWrapper<BlockchainContractType> query = Wrappers.lambdaQuery(BlockchainContractType.class).eq(BlockchainContractType::getName, name);
        return this.getOne(query);
    }


}
