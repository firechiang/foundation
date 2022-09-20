package net.foundation.mcrypto.rpc;

import net.foundation.mcrypto.exception.RpcRequestException;
import net.foundation.mcrypto.rpc.domain.*;
import net.foundation.mcrypto.utils.JsonUtil;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EthereumDefaultClient implements EthereumInterface {

    private String rpcUrl;

    private InetSocketAddress proxyAddress;

    public EthereumDefaultClient(String rpcUrl) {
        this(rpcUrl,null);
    }

    public EthereumDefaultClient(String rpcUrl,InetSocketAddress proxyAddress) {
        this.rpcUrl = rpcUrl;
        this.proxyAddress = proxyAddress;
    }

    /**
     * 获取最新区块高度
     * @return
     */
    @Override
    public BigInteger getLastBlockNumber() {
        JsonRpcStringRes res = execRequest(Eth_BlockNumber, Collections.emptyList(), JsonRpcStringRes.class);
        //return Convert.toWei("39", Convert.Unit.GWEI).toBigInteger();
        return hexToBigInteger(res.getResult());
    }
    /**
     * 获取当前链上Gas价格
     * @return
     */
    @Override
    public BigInteger getGasPrice() {
        JsonRpcStringRes res = execRequest(Eth_GasPrice, Collections.emptyList(), JsonRpcStringRes.class);
        return hexToBigInteger(res.getResult());
    }
    /**
     * 获取指定地址交易数量（该值可以当成nonce使用）
     * @param address 地址
     * @return
     */
    @Override
    public BigInteger getTransactionCount(String address) {
        JsonRpcStringRes res = execRequest(Eth_GetTransactionCount, Arrays.asList(address, "latest"), JsonRpcStringRes.class);
        return hexToBigInteger(res.getResult());
    }
    /**
     * 获取指定地址以太坊余额
     * @param address 地址
     * @return
     */
    @Override
    public BigDecimal getEthereumBalanace(String address) {
        JsonRpcStringRes res = execRequest(Eth_Balance,Arrays.asList(address,"latest"), JsonRpcStringRes.class);
        return new BigDecimal(hexToBigInteger(res.getResult())).divide(Convert.Unit.ETHER.getWeiFactor()).stripTrailingZeros();
    }

    /**
     * 获取指定高度区块信息
     * @param blockNum 区块高度
     * @return
     */
    @Override
    public EthereumBlockRes getBlockByNumber(Long blockNum) {
        return execRequest(Eth_GetBlockByNumber,Arrays.asList("0x"+Long.toHexString(blockNum),true), EthereumBlockRes.class);
    }
    /**
     * 获取指定交易收据（注意待定交易没有收据）
     * @param txHash 交易 hash
     * @return
     */
    @Override
    public EthereumTransactionReceiptRes getTransactionReceipt(String txHash) {
        return execRequest(Eth_GetTransactionReceipt, Arrays.asList(txHash), EthereumTransactionReceiptRes.class);
    }

    /**
     * 获取指定交易信息
     * @param txHash
     * @return
     */
    @Override
    public EthereumTransactionRes getTransactionByHash(String txHash) {
        return execRequest(Eth_GetTransactionByHash, Arrays.asList(txHash), EthereumTransactionRes.class);
    }

    private BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex.substring(2), 16);
    }


    private <T> T execRequest(String method, List<? extends Object> params, Class<? extends JsonRpcResponse> resClazz) {
        JsonRpcRequestBody bodyEntity = new JsonRpcRequestBody(method, params);
        String jsonStr = JsonUtil.toJSONString(bodyEntity);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonStr);
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(rpcUrl))
                                         .header("Content-Type", "application/json")
                                         .timeout(Duration.ofSeconds(10))
                                         .POST(bodyPublisher)
                                         .build();

        HttpResponse<String> response = senRequest(request);
        if(response.statusCode() != 200) {
            throw new RpcRequestException(response.statusCode(),response.body());
        }
        JsonRpcResponse jsonResponse = JsonUtil.parseObject(response.body(), resClazz);
        if(!jsonResponse.isSuccess()) {
            throw new RpcRequestException(jsonResponse.getError().getCode(),jsonResponse.getError().getMessage());
        }
        return (T)jsonResponse;
    }

    private HttpResponse<String> senRequest(HttpRequest request) {
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                                              .proxy(ProxySelector.of(proxyAddress))
                                              .followRedirects(HttpClient.Redirect.NORMAL)
                                              .connectTimeout(Duration.ofSeconds(10))
                                              .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
