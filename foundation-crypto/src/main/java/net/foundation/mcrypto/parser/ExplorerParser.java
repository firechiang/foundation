package net.foundation.mcrypto.parser;

public interface ExplorerParser<T> {

    T parse(String explorerUrl,String address);
}
