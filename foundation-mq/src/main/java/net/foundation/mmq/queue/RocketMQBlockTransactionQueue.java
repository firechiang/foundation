package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mmq.MQConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RocketMQBlockTransactionQueue implements BlockTransactionQueue {

    @Value(MQConstant.Path_Blockchain_Transaction_Topic_1)
    private String topicName;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void push(List<BlockchainTransactionInfo> infos) {
        List<Message<BlockchainTransactionInfo>> msgs = infos.stream()
                                                             .map(info -> MessageBuilder.withPayload(info).setHeader("KEYS", info.getTxHash()).build())
                                                             .collect(Collectors.toList());
        int pageSize = 100;
        int totalSize = msgs.size();
        int totalPage = (totalSize - 1) / pageSize + 1;
        for(int i=0; i<totalPage; i++) {
            int start = i * pageSize;
            int end = (i + 1) == totalPage ? totalSize : (start + pageSize);
            List<Message<BlockchainTransactionInfo>> subList = msgs.subList(start, end);
            this.rocketMQTemplate.syncSend(topicName,subList);
        }
    }
}
