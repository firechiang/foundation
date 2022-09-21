package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;

public interface BlockTransactionQueue {

    void push(BlockchainTransactionInfo info);

}
