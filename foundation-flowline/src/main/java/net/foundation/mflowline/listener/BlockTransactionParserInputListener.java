package net.foundation.mflowline.listener;

import lombok.extern.slf4j.Slf4j;
import net.foundation.mbusiness.domain.BlockchainContractInfo;
import net.foundation.mbusiness.domain.BlockchainContractTypeInfo;
import net.foundation.mbusiness.domain.BlockchainTransactionInfo;
import net.foundation.mbusiness.service.BlockchainContractService;
import net.foundation.mbusiness.service.BlockchainContractTypeService;
import net.foundation.mcrypto.decoder.AbiDecoder;
import net.foundation.mcrypto.decoder.DecodedFunctionCall;
import net.foundation.mmq.queue.BlockTransactionQueue;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_1;
import static net.foundation.mmq.MQConstant.Path_Blockchain_Transaction_Topic_2;

/**
 * 解码Input数据
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = Path_Blockchain_Transaction_Topic_1, consumerGroup = "blockchain_transaction_1_parser-input")
public class BlockTransactionParserInputListener implements RocketMQListener<BlockchainTransactionInfo> {

    @Value(Path_Blockchain_Transaction_Topic_2)
    private String topicName;

    @Autowired
    private BlockTransactionQueue blockTransactionQueue;

    @Autowired
    private BlockchainContractService blockchainContractService;

    @Autowired
    private BlockchainContractTypeService blockchainContractTypeService;

    @Override
    public void onMessage(BlockchainTransactionInfo bti) {
        if(Objects.nonNull(bti.getInput())) {
            bti.setContract(bti.getTo());
            BlockchainContractInfo bci = blockchainContractService.queryCacheByAddress(bti.getContract());
            decoderInput(bti,bci);
            bti.setInput(null);
        }
        blockTransactionQueue.push(topicName,bti);
    }

    private void decoderInput(BlockchainTransactionInfo bti,BlockchainContractInfo bci) {
        try {
            DecodedFunctionCall decodedFunction = getContractTypeDecoder(bci.getCtype(), bti.getInput());
            if(Objects.isNull(decodedFunction)) {
                decodedFunction = getContractDecoder(bci.getAbiDecoder(),bti.getInput());
            }
            if(Objects.nonNull(decodedFunction)) {
                bti.setMethod(decodedFunction.getName());
            }
        } catch(Exception e) {
            log.error("Decoder blockchain transaction input!",e);
        }
    }

    private DecodedFunctionCall getContractTypeDecoder(String ctype,String input) {
        if(Objects.nonNull(ctype)) {
            BlockchainContractTypeInfo bcti = blockchainContractTypeService.queryCacheByName(ctype);
            if(Objects.nonNull(bcti) && Objects.nonNull(bcti.getAbiDecoder())) {
                return bcti.getAbiDecoder().decodeFunctionCall(input);
            }
        }
        return null;
    }

    private DecodedFunctionCall getContractDecoder(AbiDecoder abiDecoder,String input) {
        if (Objects.nonNull(abiDecoder)) {
            return abiDecoder.decodeFunctionCall(input);
        }
        return null;
    }
}
