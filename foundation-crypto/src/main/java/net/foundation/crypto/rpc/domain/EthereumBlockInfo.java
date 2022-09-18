package net.foundation.crypto.rpc.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class EthereumBlockInfo extends JsonRpcResponse {

	private Result result;

	@Data
	public static class Result {

		//private String totalDifficulty;

		private String timestamp;

		//private String difficulty;

		//private String extraData;

		//private String gasLimit;

		//private String gasUsed;

		//private String hash;

		//private String logsBloom;

		//private String miner;

		//private String mixHash;

		//private String nonce;

		private String number;

		//private String parentHash;

		//private String receiptsRoot;

		//private String sha3Uncles;

		//private String size;

		//private String stateRoot;

		//private String transactionsRoot;

		//private List<Object> uncles;

		private List<EthereumTransactionInfo> transactions;
	}
}
