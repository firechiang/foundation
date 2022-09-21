package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import net.foundation.mbusiness.domain.BlockchainContract;
import net.foundation.mbusiness.mapper.BlockchainContractMapper;
import net.foundation.mbusiness.service.BlockchainContractService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlockchainContractServiceImpl extends ServiceImpl<BlockchainContractMapper, BlockchainContract> implements BlockchainContractService, InitializingBean {

    private Cache<String, BlockchainContract> cache;

    @Override
    public BlockchainContract queryCacheByAddress(String contractAddr) {
        return this.cache.getIfPresent(contractAddr);
    }

    @Override
    public void cache(BlockchainContract bc) {
        this.cache.put(bc.getAddr(),bc);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        BlockchainContractService service = this;
        CacheLoader<String, BlockchainContract> cacheLoader = new CacheLoader<>() {
            @Override
            public BlockchainContract load(String contractAddr) throws Exception {
                var query = Wrappers.lambdaQuery(BlockchainContract.class).eq(BlockchainContract::getAddr,contractAddr);
                return service.getOne(query);
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
