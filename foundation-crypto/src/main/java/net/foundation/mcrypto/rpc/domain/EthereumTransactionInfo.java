package net.foundation.mcrypto.rpc.domain;

import lombok.Data;

@Data
public class EthereumTransactionInfo {

    private String blockHash;

    private String blockNumber;

    private String chainId;

    private String from;

    private String gas;

    private String gasPrice;

    //private String maxFeePerGas;

    //private String maxPriorityFeePerGas;

    private String hash;

    private String input;

    private String nonce;

    private String to;

    //private String transactionIndex;

    private String value;

    private String type;

    //private String v;

    //private String r;

    //private String s;
}
