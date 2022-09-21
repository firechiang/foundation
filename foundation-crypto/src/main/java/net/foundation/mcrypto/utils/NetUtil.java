package net.foundation.mcrypto.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetUtil {

    public static InetSocketAddress telnetProxy() {
        Socket socket = new Socket();
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 58591);
            socket.connect(inetSocketAddress, 10);
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
