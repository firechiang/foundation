package net.foundation.mmq.queue;

import net.foundation.mbusiness.domain.BlockchainTransactionInfo;

import java.util.List;

public interface BlockTransactionQueue {

    void push(List<BlockchainTransactionInfo> infos);

}
