package org.beyond.rbac.model.param;

import javax.validation.constraints.NotEmpty;

/**
 * @author Beyond
 */
public class PermissionCode {

    @NotEmpty(message = "权限编码不能为空")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}
