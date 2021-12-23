package org.beyond.rbac.exception;

import org.beyond.rbac.constant.Code;

/**
 * @author Beyond
 */
public class ApiException extends RuntimeException {

    private String code;

    public ApiException() {
        this("未知错误");
    }

    public ApiException(final String message) {
        super(message);
        this.code = Code.FAILED;
    }

}
