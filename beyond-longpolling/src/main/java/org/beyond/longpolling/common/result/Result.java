package org.beyond.longpolling.common.result;

public class Result<T> implements IResult<T> {

    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = -1;

    private static final Result<Object> OK = new Result<>(CODE_OK, null, null);

    private int code;
    private String message;
    private T data;

    protected Result() {
        this(CODE_OK, null, null);
    }

    protected Result(final int code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }

    public static <T> Result<T> make(final int code, final String message, final T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(final String message) {
        return new Result<>(CODE_ERROR, message, null);
    }

    public static <T> Result<T> error(final int code, final String message) {
        return new Result<>(code, message, null);
    }

    public static Result<Object> ok() {
        return OK;
    }

    public static <T> Result<T> ok(final T data) {
        return new Result<>(CODE_OK, null, data);
    }

    public static <T> Result<T> ok(final String message) {
        return new Result<>(CODE_OK, message, null);
    }

    public static <T> Result<T> ok(final String message, final T data) {
        return new Result<>(CODE_OK, message, data);
    }

    public static <T> Result<T> data(final T data) {
        return new Result<>(CODE_OK, null, data);
    }

    public static <T> Result<T> error(final T data) {
        return new Result<>(CODE_ERROR, null, data);
    }

}
