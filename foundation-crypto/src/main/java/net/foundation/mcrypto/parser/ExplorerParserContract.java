package net.foundation.mcrypto.parser;

import net.foundation.mcrypto.parser.domain.ContractAddressInfo;
import net.foundation.mcrypto.parser.domain.ContractTokenInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

public class ExplorerParserContract extends ExplorerParserAbstract {

    /**
     * 解析Address信息
     * @param explorerUrl
     * @param addr
     */
    public ContractAddressInfo parseAddress(String explorerUrl,String addr) {
        ContractAddressInfo info = new ContractAddressInfo();
        String url = String.join("/",explorerUrl,"address",addr);
        Document document = getBrowser(url);
        // 解析合约Abi
        info.setAbi(parseAbi(document));
        Elements elements = parseTables(document);
        if(Objects.nonNull(elements)) {
            // 解析合约名称
            parseName(elements.get(1),info);
        }
        return info;
    }

    /**
     * 解析Token信息
     * @param explorerUrl
     * @param addr
     * @return
     */
    public ContractTokenInfo parseToken(String explorerUrl,String addr) {
        ContractTokenInfo info = new ContractTokenInfo();
        String url = String.join("/",explorerUrl,"token",addr);
        Document document = getBrowser(url);
        Elements elements = parseTables(document);
        if(Objects.nonNull(elements)) {
            // 解析合约类型
            parseType(elements.get(0),info);
            // 解析合约精度
            parseDecimals(elements.get(1),info);
        }
        return info;
    }

    private Elements parseTables(Document document) {
        Element element = document.selectFirst("#content");
        Element tbel = element.selectFirst("#ContentPlaceHolder1_divSummary");
        if(Objects.nonNull(tbel) && tbel.childrenSize() > 0) {
            Element tabelc = tbel.child(0);
            Elements tables = tabelc.select(".col-md-6");
            // 左右两边表格
            if(tables.size() == 2) {
                return tables;
            }
        }
        return null;
    }

    /**
     * 解析合约类型
     * @param element
     * @param info
     */
    public void parseType(Element element,ContractTokenInfo info) {
        Element headerTitle = element.selectFirst(".card-header-title");
        if(headerTitle.childrenSize() > 0) {
            String contractType = headerTitle.child(0).text();
            if(Objects.nonNull(contractType) && contractType.length() > 2) {
                info.setCtype(contractType.substring(1,contractType.length() - 1));
            }
        }
    }

    /**
     * 解析合约精度
     * @param element
     * @param info
     */
    public void parseDecimals(Element element,ContractTokenInfo info) {
        Element headerDecimals = element.selectFirst("#ContentPlaceHolder1_trDecimals");
        if(Objects.nonNull(headerDecimals)) {
            Element decimalsEl = headerDecimals.selectFirst(".col-md-8");
            if(Objects.nonNull(decimalsEl)) {
                String decimalsStr = decimalsEl.text();
                if(Objects.nonNull(decimalsStr)) {
                    info.setDecimals(Integer.parseInt(decimalsStr.trim()));
                }
            }
        }
    }

    /**
     * 解析合约名称
     * @param element
     * @param info
     */
    private void parseName(Element element,ContractAddressInfo info) {
        Element headerDecimals = element.selectFirst("#ContentPlaceHolder1_tr_tokeninfo");
        if(Objects.nonNull(headerDecimals)) {
            Element nameEl = headerDecimals.selectFirst(".col-md-8");
            if(Objects.nonNull(nameEl)) {
                Element nameA = nameEl.selectFirst("a");
                if(Objects.nonNull(nameA)) {
                    info.setName(nameA.text());
                }
            }
        }
    }

    /**
     * 解析合约Abi
     * @param document
     */
    private String parseAbi(Document document) {
        Element abiEl = document.selectFirst("#js-copytextarea2");
        if(Objects.nonNull(abiEl)) {
            return abiEl.text();
        }
        return null;
    }
}
