package net.foundation.crypto.rpc.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
public class JsonRpcResponse extends JsonRpcEntity {

	private Error error;

	public boolean isSuccess() {

		return Objects.isNull(error);
	}

	@Data
	public static class Error {

		private Integer code;

		private String message;
	}
}
