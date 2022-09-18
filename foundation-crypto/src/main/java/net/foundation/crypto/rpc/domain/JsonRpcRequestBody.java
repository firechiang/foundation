package net.foundation.crypto.rpc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class JsonRpcRequestBody extends JsonRpcEntity {

    private String method;

    private List<? extends Object> params;

	public JsonRpcRequestBody(String method, List<? extends Object> params) {
		super();
		this.method = method;
		this.params = params;
	}
}
