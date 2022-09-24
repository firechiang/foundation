package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;

import java.util.List;

public interface BlockTransactionQueue {

    void push(String topic,List<BlockchainTransactionInfo> infos);

    void push(String topic,BlockchainTransactionInfo info);

}
