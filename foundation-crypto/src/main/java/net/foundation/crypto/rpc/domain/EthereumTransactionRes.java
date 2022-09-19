package net.foundation.crypto.rpc.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class EthereumTransactionRes extends JsonRpcResponse {

    private EthereumTransactionInfo result;
}
