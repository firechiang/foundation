package net.foundation.mflowline;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_2;



/**
 * 处理数据入库
 */
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic_2, consumerGroup = "blockchain_transaction_2_storage")
public class BlockTransactionStorageListener implements RocketMQListener<BlockchainTransactionInfo> {

    @Override
    public void onMessage(BlockchainTransactionInfo info) {
    }
}
