package org.beyond.rbac.exception;

import org.beyond.rbac.constant.Code;

/**
 * @author Beyond
 */
public class DataNotFoundException extends RuntimeException {

    private String code;

    public DataNotFoundException() {
        this("找不到数据");
    }

    public DataNotFoundException(final String message) {
        super(message);
        this.code = Code.FAILED;
    }

}
