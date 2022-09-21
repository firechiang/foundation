package net.foundation.mflowline;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mmq.queue.BlockTransactionQueue;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_1;
import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_2;

/**
 * 解码Input数据
 */
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic_1, consumerGroup = "blockchain_transaction_1_parser-input")
public class BlockTransactionParserInputListener implements RocketMQListener<BlockchainTransactionInfo> {

    @Value(Path_Blockchain_Transaction_Topic_2)
    private String topicName;

    @Autowired
    private BlockTransactionQueue blockTransactionQueue;

    @Override
    public void onMessage(BlockchainTransactionInfo blockchainTransactionInfo) {
        System.err.println(blockchainTransactionInfo);
    }
}
