package net.foundation.crypto.rpc;

import net.foundation.crypto.rpc.domain.EthereumBlockInfo;
import net.foundation.crypto.rpc.domain.EthereumTransactionReceiptInfo;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface EthereumInterface {
    // 获取指定高度区块信息
    String Eth_GetBlockByNumber = "eth_getBlockByNumber";
    // 发送已签名交易
    String Eth_SendRawTransaction = "eth_sendRawTransaction";
    // 获取指定地址交易数量
    String Eth_GetTransactionCount = "eth_getTransactionCount";
    // 获取指定交易收据，注意待定交易没有收据
    String Eth_GetTransactionReceipt = "eth_getTransactionReceipt";
    // 获取当前链上Gas价格
    String Eth_GasPrice = "eth_gasPrice";
    // 预估 GasLimit
    String Eth_EstimateGas = "eth_estimateGas";
    // 获取最新区块高度
    String Eth_BlockNumber = "eth_blockNumber";
    // 获取指定地址余额
    String Eth_Balance = "eth_getBalance";


    /**
     * 获取最新区块高度
     * @return
     */
    BigInteger getLastBlockNumber();
    /**
     * 获取当前链上Gas价格
     * @return
     */
    BigInteger getGasPrice();
    /**
     * 获取指定地址交易数量（该值可以当成nonce使用）
     * @param address 地址
     * @return
     */
    BigInteger getTransactionCount(String address);
    /**
     * 获取指定地址以太坊余额
     * @param address 地址
     * @return
     */
    BigDecimal getEthereumBalanace(String address);
    /**
     * 获取指定高度区块信息
     * @param blockNum 区块高度
     * @return
     */
    EthereumBlockInfo getBlockByNumber(Long blockNum);
    /**
     * 获取指定交易收据（注意待定交易没有收据）
     * @param txhash 交易 hash
     * @return
     */
    EthereumTransactionReceiptInfo getTransactionReceipt(String txhash);

    /**
     * 发送已签名交易
     * @param hex 已签名交易信息
     * @return
     */
    default String sendRawTransaction(String hex) {
        return null;
    }
}
