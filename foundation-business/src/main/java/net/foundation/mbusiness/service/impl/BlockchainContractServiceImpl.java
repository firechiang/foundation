package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import net.foundation.mbusiness.domain.BlockchainContract;
import net.foundation.mbusiness.domain.BlockchainContractInfo;
import net.foundation.mbusiness.mapper.BlockchainContractMapper;
import net.foundation.mbusiness.service.BlockchainContractService;
import net.foundation.mcrypto.decoder.AbiDecoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlockchainContractServiceImpl extends ServiceImpl<BlockchainContractMapper, BlockchainContract> implements BlockchainContractService, InitializingBean {

    private Cache<String, BlockchainContractInfo> cache;

    @Override
    public BlockchainContractInfo queryCacheByAddress(String contractAddr) {
        return this.cache.getIfPresent(contractAddr);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BlockchainContractService service = this;
        CacheLoader<String, BlockchainContractInfo> cacheLoader = new CacheLoader<>() {
            @Override
            public BlockchainContractInfo load(String contractAddr) throws Exception {
                var query = Wrappers.lambdaQuery(BlockchainContract.class).eq(BlockchainContract::getAddr,contractAddr);
                BlockchainContract bc = service.getOne(query);
                if(Objects.nonNull(bc)) {
                    BlockchainContractInfo info = new BlockchainContractInfo();
                    BeanUtils.copyProperties(bc,info);
                    if(StringUtils.isNoneBlank(bc.getAbi())) {
                        info.setAbiDecoder(new AbiDecoder(bc.getAbi()));
                    }
                    return info;
                }
                return null;
            }
        };
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(50)
                                 .expireAfterWrite(3, TimeUnit.HOURS)
                                 .initialCapacity(200)
                                 .maximumSize(1000)
                                 .build(cacheLoader);
    }
}
