package net.foundation.mcrypto;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Arrays;

public class SignTest {

    private String privateKey;
    private String address;

    @Before
    public void createAddr() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        Credentials credentials = Credentials.create(ecKeyPair);
        this.address = credentials.getAddress();
        this.privateKey = Hex.toHexString(ecKeyPair.getPrivateKey().toByteArray());
    }

    @Test
    public void test() throws SignatureException {
        /**----------------------------------原文信息-------------------------------------------------*/
        //原文
        String message = "Sign";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] messageHash = Hash.sha3(messageBytes);

        /**----------------------------------签名部分-------------------------------------------------*/
        // 私钥
        ECKeyPair eCKeyPair = ECKeyPair.create(Hex.decode(this.privateKey));
        // 签名（注意：这个函数在签名前改变力原文内容，添加了ETH签名头信息，使原文的内容变成了这样: "\x19Ethereum Signed Message:\n" + len(message) + message ）
        //Sign.SignatureData signMessage = Sign.signPrefixedMessage(messageHash,eCKeyPair);
        // 签名（注意：这个签名函数没有改变原文的内容，只是将原文的内容进行了一次 Sha3 转换）
        Sign.SignatureData signMessage = Sign.signMessage(messageBytes,eCKeyPair);
        // 签名后字符串
        String signature = "0x" + Hex.toHexString(signMessage.getR()) + Hex.toHexString(signMessage.getS()) + Hex.toHexString(signMessage.getV());
        System.out.println(signature);

        /**----------------------------------验证签名-------------------------------------------------*/
        Sign.SignatureData signatureData = toSignatureData(signature);
        // 原文信息添加ETH签名头信息（注意：因为签名前改变了原文的内容，所以验证签名也要改变原文的内容）
        //byte[] messageHashBytes = Sign.getEthereumMessageHash(messageHash);
        // 原文信息将内容进行一次Sha3转换，因为签名时进行了一转换
        byte[] messageHashBytes = Hash.sha3(messageBytes);
        // 解析签名公钥
        BigInteger publicKey = Sign.signedMessageHashToKey(messageHashBytes, signatureData);

        String address1 = Numeric.prependHexPrefix(Keys.getAddress(publicKey));
        System.err.println(address);
        System.err.println(address1);
        System.err.println("签名验证:"+this.address.equals(address1));
    }

    private Sign.SignatureData toSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        return new Sign.SignatureData(v,Arrays.copyOfRange(signatureBytes, 0, 32),Arrays.copyOfRange(signatureBytes, 32, 64));
    }
}
