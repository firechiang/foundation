package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mmq.MQConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class RocketMQBlockTransactionQueue implements BlockTransactionQueue {

    @Value(MQConstant.Path_Blockchain_Transaction_Topic_1)
    private String topicName;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void push(BlockchainTransactionInfo info) {
        Message<BlockchainTransactionInfo> msg = MessageBuilder.withPayload(info)
                                                               .setHeader("KEYS",info.getTxHash())
                                                               .build();
        this.rocketMQTemplate.syncSend(topicName,msg);
    }
}
