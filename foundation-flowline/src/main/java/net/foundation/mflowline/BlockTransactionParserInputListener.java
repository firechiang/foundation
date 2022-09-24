package net.foundation.mflowline;

import lombok.extern.slf4j.Slf4j;
import net.foundation.mbusiness.domain.*;
import net.foundation.mbusiness.service.BlockchainContractService;
import net.foundation.mbusiness.service.BlockchainContractTypeService;
import net.foundation.mbusiness.service.BlockchainService;
import net.foundation.mcrypto.decoder.AbiDecoder;
import net.foundation.mcrypto.decoder.DecodedFunctionCall;
import net.foundation.mcrypto.parser.ExplorerParserContract;
import net.foundation.mcrypto.parser.domain.ContractInfo;
import net.foundation.mmq.queue.BlockTransactionQueue;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    private BlockchainService blockchainService;

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
            BlockchainContractInfo bci = this.getContractInfo(bti.getChainId(),bti.getContract());
            decoderInput(bti,bci);
            bti.setInput(null);
        }
        blockTransactionQueue.push(topicName,bti);
    }

    private BlockchainContractInfo getContractInfo(Integer chainId,String contractAddr) {
        try {
            BlockchainContractInfo contractInfo = blockchainContractService.queryCacheByAddress(contractAddr);
            // 数据库不存在合约信息
            if (Objects.isNull(contractInfo.getId())) {
                contractInfo.setAddr(contractAddr);
                BlockchainInfo blockchainInfo = blockchainService.queryCacheByChainId(chainId);
                loadExplorerContract(contractInfo, blockchainInfo.getExplorerUrl());
            // 数据库存在合约信息但是没有ABI
            } else if (contractInfo.isLoadDisk() && Objects.isNull(contractInfo.getAbiDecoder())) {
                BlockchainInfo blockchainInfo = blockchainService.queryCacheByChainId(chainId);
                loadExplorerABI(contractInfo, blockchainInfo.getExplorerUrl());
            }
            return contractInfo;
        } catch(Exception e) {
            log.error("Get blockchain contract info!",e);
        }
        return new BlockchainContractInfo();
    }

    private void loadExplorerContract(BlockchainContractInfo contractInfo,String explorerUrl) {
        ExplorerParserContract epc = new ExplorerParserContract();
        ContractInfo parse = epc.parse(explorerUrl, contractInfo.getAddr());
        BlockchainContract bc = new BlockchainContract();
        bc.setAbi(parse.getAbi());
        bc.setAddr(contractInfo.getAddr());
        bc.setCtype(parse.getCtype());
        bc.setDecimals(parse.getDecimals());
        bc.setName(parse.getName());
        bc.setCreateTime(new Date());
        blockchainContractService.saveIgnore(bc);
        BeanUtils.copyProperties(bc,contractInfo);
        contractInfo.setAbiDecoder(AbiDecoder.create(bc.getAbi()));
    }

    private void loadExplorerABI(BlockchainContractInfo contractInfo,String explorerUrl) {
        ExplorerParserContract epc = new ExplorerParserContract();
        String abi = epc.parseAbi(explorerUrl, contractInfo.getAddr());
        contractInfo.setAbiDecoder(AbiDecoder.create(abi));
        if(Objects.nonNull(contractInfo.getAbiDecoder())) {
            BlockchainContract bc = new BlockchainContract();
            bc.setId(contractInfo.getId());
            bc.setAbi(abi);
            blockchainContractService.updateById(bc);
            contractInfo.setLoadDisk(false);
        }
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
