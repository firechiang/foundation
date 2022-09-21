package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import net.foundation.mbusiness.domain.Blockchain;
import net.foundation.mbusiness.mapper.BlockchainMapper;
import net.foundation.mbusiness.service.BlockchainService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlockchainServiceImpl extends ServiceImpl<BlockchainMapper, Blockchain> implements BlockchainService, InitializingBean {

    private Cache<Integer,Blockchain> cache;

    @Override
    public Blockchain queryCacheByChainId(Integer chainId) {
        return this.cache.getIfPresent(chainId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BlockchainService service = this;
        CacheLoader<Integer, Blockchain> cacheLoader = new CacheLoader<>() {
            @Override
            public Blockchain load(Integer chainId) throws Exception {
                var query = Wrappers.lambdaQuery(Blockchain.class).eq(Blockchain::getChainId,chainId);
                return service.getOne(query);
            }
        };
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(20)
                                 .expireAfterWrite(30, TimeUnit.MINUTES)
                                 .initialCapacity(10)
                                 .maximumSize(10)
                                 .build(cacheLoader);
    }
}
