package net.foundation.mflowline;

import net.foundation.mbusiness.domain.BlockchainTransaction;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static net.foundation.mbusiness.BusinessConstant.Path_Blockchain_Transaction_Topic;



/**
 * 处理数据入库
 */
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic, consumerGroup = "blockchain_transaction_storage")
public class BlockTransactionStorageListener implements RocketMQListener<BlockchainTransaction> {

    @Override
    public void onMessage(BlockchainTransaction info) {
    }
}
