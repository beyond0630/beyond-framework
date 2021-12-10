package org.beyond.rabc.model.param;

import javax.validation.constraints.NotEmpty;

/**
 * @author Beyond
 */
public class RoleCode {

    @NotEmpty(message = "角色编码不能为空")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}
