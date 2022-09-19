package net.foundation.crypto.rpc.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class EthereumTransactionReceiptRes extends JsonRpcResponse {

	private Result result;

	@Data
	public static class Result {
		/**
		 * 交易哈希
		 */
		private String transactionHash;
		/**
		 * 交易在区块内的序号
		 */
		private String transactionIndex;
		/**
		 * 区块哈希
		 */
		private String blockHash;
		/**
		 * 区块高度
		 */
		private String blockNumber;
		/**
		 * 合约地址
		 */
		private String contractAddress;
		/**
		 * 该交易执行时区块gas总用量
		 */
		private String cumulativeGasUsed;
		/**
		 * 发起地址
		 */
		private String from;
		/**
		 * 目标地址
		 */
		private String to;
		/**
		 * 交易gas用量
		 */
		private String gasUsed;
		/**
		 * 用于快速提取相关日志的布隆过滤器
		 */
		private String logsBloom;
		/**
		 * 交易产生的日志数组
		 */
		private List<Object> logs;
		/**
		 * 交易后状态根（pre Byzantium）
		 */
		private String root;
		/**
		 * 交易执行状态，1 - success 0 - failure
		 */
		private String status;

		public boolean isStatus() {
			if("0x1".equals(status)) {
				return true;
			}
			return false;
		}
	}
}
