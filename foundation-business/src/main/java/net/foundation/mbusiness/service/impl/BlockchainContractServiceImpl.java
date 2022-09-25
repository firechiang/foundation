package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class BlockchainContractServiceImpl extends ServiceImpl<BlockchainContractMapper, BlockchainContract> implements BlockchainContractService {

    private final LoadingCache<String, BlockchainContractInfo> cache;

    public BlockchainContractServiceImpl() {
        BlockchainContractService service = this;
        CacheLoader<String, BlockchainContractInfo> cacheLoader = new CacheLoader<>() {
            @Override
            public BlockchainContractInfo load(String contractAddr) throws Exception {
                BlockchainContract bc = service.queryByAddress(contractAddr);
                BlockchainContractInfo info = new BlockchainContractInfo();
                info.setLoadDisk(true);
                if(Objects.nonNull(bc)) {
                    BeanUtils.copyProperties(bc,info);
                    if(StringUtils.isNoneBlank(bc.getAbi())) {
                        info.setAbiDecoder(AbiDecoder.create(bc.getAbi()));
                    }
                }
                return info;
            }
        };
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(50)
                                 .expireAfterWrite(3, TimeUnit.MINUTES)
                                 .initialCapacity(200)
                                 .maximumSize(1000)
                                 .build(cacheLoader);
    }

    @Override
    public BlockchainContractInfo queryCacheByAddress(String contractAddr) {
        try {
            return this.cache.get(contractAddr);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCache(String contractAddr) {
        this.cache.invalidate(contractAddr);
    }

    @Override
    public BlockchainContract queryByAddress(String contractAddr) {
        var query = Wrappers.lambdaQuery(BlockchainContract.class).eq(BlockchainContract::getAddr,contractAddr);
        return this.getOne(query);
    }

    @Override
    public int saveIgnore(BlockchainContract bc) {
        return this.baseMapper.insertIgnore(bc);
    }
}
