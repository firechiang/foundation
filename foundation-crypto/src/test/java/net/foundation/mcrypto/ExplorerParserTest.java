package net.foundation.mcrypto;

import net.foundation.mcrypto.parser.ExplorerParserContract;
import net.foundation.mcrypto.parser.domain.ContractAddressInfo;
import net.foundation.mcrypto.parser.domain.ContractTokenInfo;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExplorerParserTest {
    @Test
    public void parseAddress() {

        ExplorerParserContract epc = new ExplorerParserContract("https://etherscan.io");
        ContractAddressInfo cai = epc.parseAddress("0xdac17f958d2ee523a2206206994597c13d831ec7");
        System.err.println(cai);
    }

    @Test
    public void parseToken() {
        ExplorerParserContract epc = new ExplorerParserContract("https://etherscan.io");
        ContractTokenInfo cti = epc.parseToken("0xdac17f958d2ee523a2206206994597c13d831ec7");
        System.err.println(cti);
    }
}
