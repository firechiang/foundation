package net.foundation.mfetch.work;

import lombok.extern.slf4j.Slf4j;
import net.foundation.mbusiness.domain.Blockchain;
import net.foundation.mbusiness.domain.BlockchainInfo;
import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mbusiness.service.BlockchainService;
import net.foundation.mcrypto.rpc.EthereumDefaultClient;
import net.foundation.mcrypto.rpc.EthereumInterface;
import net.foundation.mcrypto.rpc.domain.EthereumBlockRes;
import net.foundation.mcrypto.rpc.domain.EthereumTransactionInfo;
import net.foundation.mcrypto.utils.NetUtil;
import net.foundation.mmq.queue.BlockTransactionQueue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class EthereumSyncWork {

    private BlockTransactionQueue blockTransactionQueue;

    private BlockchainService blockchainService;

    private EthereumInterface api;

    private BlockchainInfo blockchain;

    private volatile long oldHeight = 0;

    private String topicName;

    public EthereumSyncWork(BlockTransactionQueue blockTransactionQueue, BlockchainService blockchainService, BlockchainInfo blockchain, String topicName) {
        this.blockTransactionQueue = blockTransactionQueue;
        this.blockchainService = blockchainService;
        this.blockchain = blockchain;
        this.api = getEthereumDefaultClient();
        this.topicName = topicName;
        initOldHeight();
    }


    public void execute() {
        BlockchainInfo newBlockchain = blockchainService.queryCacheByChainId(this.blockchain.getChainId());
        if(Objects.nonNull(newBlockchain) && this.blockchain != newBlockchain) {
            this.blockchain = newBlockchain;
            this.api = getEthereumDefaultClient();
        }
        EthereumBlockRes res = api.getBlockByNumber(oldHeight + 1);
        if(Objects.nonNull(res.getResult())) {
            long newHeight = Long.parseLong(res.getResult().getNumber().substring(2),16);
            if (oldHeight != newHeight) {
                List<BlockchainTransactionInfo> bts = res.getResult().getTransactions().stream().map(this::toBlockchainTransaction).collect(Collectors.toList());
                log.info("Ethereum height: {},blockCount: 1,transaction: {}",newHeight,bts.size());
                blockTransactionQueue.push(this.topicName,bts);
                saveLastHeight(oldHeight);
            }
            oldHeight++;
        }
        if(Objects.nonNull(res.getError())) {
            log.error(res.toString());
        }
    }

    private BlockchainTransactionInfo toBlockchainTransaction(EthereumTransactionInfo info) {
        BlockchainTransactionInfo bti = new BlockchainTransactionInfo();
        bti.setChainId(this.blockchain.getChainId());
        bti.setForm(info.getFrom());
        bti.setTo(info.getTo());
        bti.setTxHash(info.getHash());
        bti.setInput(info.getInput());
        bti.setAmount(new BigDecimal(api.hexToBigInteger(info.getValue())).divide(blockchain.getDecimalsPow()).setScale(8, RoundingMode.DOWN));
        return bti;
    }

    private void initOldHeight() {
        this.oldHeight = blockchain.getLastHeight();
        if(blockchain.getLastHeight() == 0) {
            this.oldHeight = api.getLastBlockNumber().longValue();
        }
    }

    private EthereumDefaultClient getEthereumDefaultClient() {
        return new EthereumDefaultClient(this.blockchain.getRpcUrl(), NetUtil.telnetProxy());
    }

    private void saveLastHeight(long oldHeight) {
        if(oldHeight % 10l == 0) {
            Blockchain bc = new Blockchain();
            bc.setId(this.blockchain.getId());
            bc.setLastHeight(oldHeight);
            this.blockchainService.updateById(bc);
        }
    }
}

