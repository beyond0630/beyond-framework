package org.beyond.rabc.result;

import org.beyond.rabc.constant.Code;

/**
 * @author Beyond
 */
public class Result<T> implements IResult<T> {


    private static final Result<Object> OK = new Result<>(Code.SUCCESS, "操作成功", null);

    private String code;
    private String message;
    private T data;

    protected Result() {
        this(Code.SUCCESS, null, null);
    }

    protected Result(final String code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String getCode() {
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

    public static <T> Result<T> make(final String code, final String message, final T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(final String message) {
        return new Result<>(Code.FAILED, message, null);
    }

    public static <T> Result<T> error(final String code, final String message) {
        return new Result<>(code, message, null);
    }

    public static Result<Object> ok() {
        return OK;
    }

    public static <T> Result<T> ok(final T data) {
        return new Result<>(Code.SUCCESS, null, data);
    }

    public static <T> Result<T> ok(final String message) {
        return new Result<>(Code.SUCCESS, message, null);
    }

    public static <T> Result<T> ok(final String message, final T data) {
        return new Result<>(Code.SUCCESS, message, data);
    }

    public static <T> Result<T> data(final T data) {
        return new Result<>(Code.SUCCESS, "操作成功", data);
    }

    public static <T> Result<T> error(final T data) {
        return new Result<>(Code.FAILED, null, data);
    }

}
