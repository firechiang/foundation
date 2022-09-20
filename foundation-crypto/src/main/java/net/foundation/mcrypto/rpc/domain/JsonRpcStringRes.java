package net.foundation.mcrypto.rpc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class JsonRpcStringRes extends JsonRpcResponse {

	private String result;
}
