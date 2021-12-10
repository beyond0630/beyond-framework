package org.beyond.rabc.result;

public interface IResult<T> {

    String getCode();

    String getMessage();

    T getData();

}
