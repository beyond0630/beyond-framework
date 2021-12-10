package org.beyond.rabc.model.param;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author Beyond
 */
public class SaveUserParams implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱地址不符合规范")
    private String email;

    @NotEmpty(message = "密码不能为空")
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
