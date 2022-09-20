package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.foundation.mbusiness.domain.Blockchain;
import net.foundation.mbusiness.mapper.BlockchainMapper;
import net.foundation.mbusiness.service.BlockchainService;
import org.springframework.stereotype.Service;

@Service
public class BlockchainServiceImpl extends ServiceImpl<BlockchainMapper, Blockchain> implements BlockchainService {

}
