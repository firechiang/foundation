package net.foundation.crypto.rpc;

import net.foundation.crypto.rpc.domain.EthereumBlockRes;
import net.foundation.crypto.rpc.domain.EthereumTransactionRes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class EthereumDefaultClientTest {

    private EthereumInterface api;

    @Before
    public void before() {
        InetSocketAddress proxyAddress = telnet();
        this.api = new EthereumDefaultClient("https://mainnet.infura.io/v3/320c254cfd1c4f34b99cadd89ed80c4c",proxyAddress);
    }

    @Test
    public void getLastBlockNumber() {
        BigInteger lastBlockNumber = api.getLastBlockNumber();
        System.err.println(lastBlockNumber);
    }

    @Test
    public void getGasPrice() {
        BigInteger gasPrice = api.getGasPrice();
        System.err.println(gasPrice);
    }

    @Test
    public void getTransactionCount() {
        BigInteger transactionCount = api.getTransactionCount("0xe688b84b23f322a994A53dbF8E15FA82CDB71127");
        System.err.println(transactionCount);
    }

    @Test
    public void getEthereumBalanace() {
        BigDecimal ethereumBalanace = api.getEthereumBalanace("0xe688b84b23f322a994A53dbF8E15FA82CDB71127");
        System.err.println(ethereumBalanace);
    }

    @Test
    public void getBlockByNumber() {
        EthereumBlockRes blockInfo = api.getBlockByNumber(15560000l);
        System.err.println(blockInfo.getResult().getTransactions().size());
    }

    @Test
    public void getTransactionByHash() {
        EthereumTransactionRes transactionRes = api.getTransactionByHash("0x1cf6e7d55a91af7da03ee13926b2570929349f9028838c32ed9d049c8d20d2ad");
        System.err.println(transactionRes);
    }

    private static InetSocketAddress telnet() {
        Socket socket = new Socket();
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 58591);
            socket.connect(inetSocketAddress, 200);
            if (socket.isConnected()) {
                return inetSocketAddress;
            }
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return null;
    }
}
