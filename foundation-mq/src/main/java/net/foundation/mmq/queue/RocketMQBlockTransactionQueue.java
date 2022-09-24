package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RocketMQBlockTransactionQueue implements BlockTransactionQueue {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private int kSize = 2;
    private int maxInputLength = 1024 * kSize;
    // 消息最大长度32K
    private int pageSize = 32 / kSize;


    @Override
    public void push(String topic,List<BlockchainTransactionInfo> infos) {
        List<Message<BlockchainTransactionInfo>> msgs = infos.stream()
                                                             .map(this::toMsg)
                                                             .collect(Collectors.toList());
        int totalSize = msgs.size();
        int totalPage = (totalSize - 1) / this.pageSize + 1;
        for(int i=0; i<totalPage; i++) {
            int start = i * this.pageSize;
            int end = (i + 1) == totalPage ? totalSize : (start + this.pageSize);
            List<Message<BlockchainTransactionInfo>> subList = msgs.subList(start, end);
            this.rocketMQTemplate.syncSend(topic,subList);
        }
    }

    @Override
    public void push(String topic,BlockchainTransactionInfo info) {
        Message<BlockchainTransactionInfo> msg = toMsg(info);
        this.rocketMQTemplate.syncSend(topic,msg);
    }

    private Message<BlockchainTransactionInfo> toMsg(BlockchainTransactionInfo info) {
        String input = info.getInput();
        if(Objects.nonNull(input)) {
            int length = input.length();
            if(length > maxInputLength || (length - 10) % 64 != 0) {
                info.setInput(null);
            }
        }
        return MessageBuilder.withPayload(info).setHeader("KEYS", info.getTxHash()).build();
    }

}
