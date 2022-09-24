package net.foundation.mfetch;

import net.foundation.mbusiness.domain.BlockchainInfo;
import net.foundation.mbusiness.service.BlockchainService;
import net.foundation.mmq.MQConstant;
import net.foundation.mmq.queue.BlockTransactionQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FetchScheduler {

    @Value(MQConstant.Path_Blockchain_Transaction_Topic_1)
    private String topicName;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private BlockTransactionQueue blockTransactionQueue;

    private EthereumSyncWork ethereumSyncWork;


    @Scheduled(fixedDelay = 1000,initialDelay = 5000)
    public void ethereumBlockScan() {
        if(Objects.isNull(ethereumSyncWork)) {
            BlockchainInfo blockchainInfo = blockchainService.queryCacheByChainId(1);
            this.ethereumSyncWork = new EthereumSyncWork(blockTransactionQueue,blockchainService,blockchainInfo,topicName);
        }
        this.ethereumSyncWork.execute();
    }
}
