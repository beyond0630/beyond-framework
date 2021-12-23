package org.beyond.rbac.result;

public interface IResult<T> {

    String getCode();

    String getMessage();

    T getData();

}
