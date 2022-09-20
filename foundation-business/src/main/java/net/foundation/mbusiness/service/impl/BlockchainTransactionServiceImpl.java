package net.foundation.mbusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.foundation.mbusiness.domain.BlockchainTransaction;
import net.foundation.mbusiness.mapper.BlockchainTransactionMapper;
import net.foundation.mbusiness.service.BlockchainTransactionService;
import org.springframework.stereotype.Service;

@Service
public class BlockchainTransactionServiceImpl extends ServiceImpl<BlockchainTransactionMapper, BlockchainTransaction> implements BlockchainTransactionService {

}
