package net.foundation.crypto.rpc.domain;

import lombok.Data;

@Data
public class JsonRpcEntity {

	private String jsonrpc = "2.0";

	private Integer id = 1;
}
