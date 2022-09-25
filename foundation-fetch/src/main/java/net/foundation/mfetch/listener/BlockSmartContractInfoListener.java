package net.foundation.mfetch.listener;

import lombok.extern.slf4j.Slf4j;
import net.foundation.mbusiness.domain.BlockchainContract;
import net.foundation.mbusiness.domain.BlockchainContractInfo;
import net.foundation.mbusiness.domain.BlockchainInfo;
import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mbusiness.service.BlockchainContractService;
import net.foundation.mbusiness.service.BlockchainService;
import net.foundation.mcrypto.parser.ExplorerParserContract;
import net.foundation.mcrypto.parser.domain.ContractAddressInfo;
import net.foundation.mcrypto.parser.domain.ContractTokenInfo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_1;

/**
 * 处理智能合约信息
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic_1, consumerGroup = "blockchain_transaction_1_smart-contract-info")
public class BlockSmartContractInfoListener implements RocketMQListener<BlockchainTransactionInfo> {

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private BlockchainContractService blockchainContractService;

    private long sleepTime = 2000;

    @Override
    public void onMessage(BlockchainTransactionInfo bti) {
        try {
            if(Objects.nonNull(bti.getInput())) {
                String contractAddr = bti.getTo();
                BlockchainContractInfo bci = blockchainContractService.queryCacheByAddress(contractAddr);
                if (bci.isLoadDisk()) {
                    BlockchainContract bc = new BlockchainContract();
                    bc.setId(bci.getId());
                    ExplorerParserContract epc = new ExplorerParserContract();
                    BlockchainInfo blockchainInfo = blockchainService.queryCacheByChainId(bti.getChainId());
                    // 解析合约地址信息
                    if(Objects.isNull(bci.getName()) || Objects.isNull(bci.getAbiDecoder())) {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                        ContractAddressInfo addressInfo = epc.parseAddress(blockchainInfo.getExplorerUrl(),contractAddr);
                        bc.setName(addressInfo.getName());
                        bc.setAbi(addressInfo.getAbi());
                    }
                    // 解析合约Token信息
                    if(Objects.isNull(bci.getDecimals())) {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                        ContractTokenInfo tokenInfo = epc.parseToken(blockchainInfo.getExplorerUrl(),contractAddr);
                        bc.setDecimals(tokenInfo.getDecimals());
                        bc.setCtype(tokenInfo.getCtype());
                    }
                    if(Objects.nonNull(bc.getName()) || Objects.nonNull(bc.getAbi()) || Objects.nonNull(bc.getDecimals())) {
                        if(Objects.isNull(bc.getId())) {
                            bc.setAddr(contractAddr);
                            bc.setCreateTime(new Date());
                            blockchainContractService.saveIgnore(bc);
                        }else{
                            blockchainContractService.updateById(bc);
                        }
                    }
                    bci.setLoadDisk(false);
                }
            }
        } catch(Exception e) {
            log.error("Get blockchain contract info!",e);
        }
    }
}
