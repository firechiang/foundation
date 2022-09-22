package net.foundation.mbusiness.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class BlockchainTransactionInfo extends BlockchainTransaction {

    private Integer chainId;

    private String input;
}
