package net.foundation.mbusiness.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.foundation.mcrypto.decoder.AbiDecoder;

@Getter
@Setter
@ToString(callSuper = true)
public class BlockchainContractTypeInfo extends BlockchainContractType {

    private AbiDecoder abiDecoder;
}
