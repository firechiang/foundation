package net.foundation.mcrypto.exception;

public class RpcRequestException extends RuntimeException {

    private Integer code;

    public RpcRequestException() {
        super();
    }

    public RpcRequestException(String message) {
        this(null,message);
    }

    public RpcRequestException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public RpcRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcRequestException(Throwable cause) {
        super(cause);
    }

    protected RpcRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getCode() {
        return code;
    }
}
