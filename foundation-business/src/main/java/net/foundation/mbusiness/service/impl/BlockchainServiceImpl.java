package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.foundation.mbusiness.domain.Blockchain;
import net.foundation.mbusiness.domain.BlockchainInfo;
import net.foundation.mbusiness.mapper.BlockchainMapper;
import net.foundation.mbusiness.service.BlockchainService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlockchainServiceImpl extends ServiceImpl<BlockchainMapper, Blockchain> implements BlockchainService {

    private LoadingCache<Integer, BlockchainInfo> cache;

    public BlockchainServiceImpl() {
        BlockchainService service = this;
        CacheLoader<Integer, BlockchainInfo> cacheLoader = new CacheLoader<>() {
            @Override
            public BlockchainInfo load(Integer chainId) throws Exception {
                Blockchain blockchain = service.queryByChainId(chainId);
                if(Objects.nonNull(blockchain)) {
                    BlockchainInfo info = new BlockchainInfo();
                    BeanUtils.copyProperties(blockchain,info);
                    info.setDecimalsPow(BigDecimal.TEN.pow(blockchain.getDecimals()));
                    return info;
                }
                return null;
            }
        };
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(20)
                                 .expireAfterWrite(30, TimeUnit.MINUTES)
                                 .initialCapacity(10)
                                 .maximumSize(10)
                                 .build(cacheLoader);
    }

    @Override
    public BlockchainInfo queryCacheByChainId(Integer chainId) {
        try {
            return this.cache.get(chainId);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Blockchain queryByChainId(Integer chainId) {
        var query = Wrappers.lambdaQuery(Blockchain.class).eq(Blockchain::getChainId,chainId);
        return this.getOne(query);
    }
}
