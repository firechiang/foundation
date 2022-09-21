package net.foundation.mbusiness;

public enum ContractType {

    OTHER(0),

    ERC20(1),

    ERC721(2);

    private Integer value;

    ContractType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
