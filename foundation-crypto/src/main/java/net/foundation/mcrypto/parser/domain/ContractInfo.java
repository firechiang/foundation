package net.foundation.mcrypto.parser.domain;

import lombok.Data;

@Data
public class ContractInfo {

    private String name;

    private String ctype;

    private Integer decimals;

    private String abi;
}
