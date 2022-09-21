package net.foundation.mcrypto.parser;

import net.foundation.mcrypto.utils.NetUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;

public abstract class ExplorerParserAbstract {

    private static final String UserAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";

    protected Document getBrowser(String url) {
        Connection connect = Jsoup.connect(url).timeout(10000).userAgent(UserAgent);
        // 设置代理
        InetSocketAddress inetSocketAddress = NetUtil.telnetProxy();
        if (null != inetSocketAddress) {
            connect.proxy(new Proxy(Proxy.Type.HTTP,inetSocketAddress));
        }
        try {
            return connect.get();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
