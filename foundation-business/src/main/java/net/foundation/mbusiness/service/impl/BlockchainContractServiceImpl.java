package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.foundation.mbusiness.domain.BlockchainContract;
import net.foundation.mbusiness.domain.BlockchainContractInfo;
import net.foundation.mbusiness.mapper.BlockchainContractMapper;
import net.foundation.mbusiness.service.BlockchainContractService;
import net.foundation.mcrypto.decoder.AbiDecoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlockchainContractServiceImpl extends ServiceImpl<BlockchainContractMapper, BlockchainContract> implements BlockchainContractService {

    private Cache<String, BlockchainContractInfo> cache = CacheBuilder.newBuilder()
                                                                      .concurrencyLevel(50)
                                                                      .expireAfterWrite(3, TimeUnit.HOURS)
                                                                      .initialCapacity(200)
                                                                      .maximumSize(1000)
                                                                      .build();

    @Override
    public BlockchainContractInfo queryCacheByAddress(String contractAddr) {
        BlockchainContractInfo info = this.cache.getIfPresent(contractAddr);
        if(Objects.isNull(info)) {
            var query = Wrappers.lambdaQuery(BlockchainContract.class).eq(BlockchainContract::getAddr,contractAddr);
            BlockchainContract bc = this.getOne(query);
            if(Objects.nonNull(bc)) {
                info = new BlockchainContractInfo();
                BeanUtils.copyProperties(bc,info);
                if(StringUtils.isNoneBlank(bc.getAbi())) {
                    info.setAbiDecoder(new AbiDecoder(bc.getAbi()));
                }
                this.cache.put(contractAddr,info);
            }
        }
        return info;
    }

    @Override
    public void removeCache(String contractAddr) {
        this.cache.invalidate(contractAddr);
    }
}
