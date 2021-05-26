package org.beyond.longpolling.common.result;

public interface IResult<T> {

    int getCode();

    String getMessage();

    T getData();

}
