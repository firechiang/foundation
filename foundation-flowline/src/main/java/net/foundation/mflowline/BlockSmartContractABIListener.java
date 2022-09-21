package net.foundation.mflowline;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static net.foundation.mbusiness.BusinessConstant.Path_Blockchain_Transaction_Topic;

/**
 * 处理智能合约ABI
 */
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic, consumerGroup = "blockchain_transaction_smart-contract-abi")
public class BlockSmartContractABIListener implements RocketMQListener<BlockchainTransactionInfo> {

    @Override
    public void onMessage(BlockchainTransactionInfo info) {

    }

}
