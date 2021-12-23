package org.beyond.rbac.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 用户角色表
 */
@Entity
@Table(name = "br_user_role")
@DynamicInsert
@DynamicUpdate
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户 id
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 角色编码
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    /**
     * 是否删除
     */
    @Column(name = "is_disabled", nullable = false)
    private Boolean disabled;

    /**
     * id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 用户 id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 用户 id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 角色编码
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 角色编码
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 是否删除
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * 是否删除
     */
    public Boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return "UserRole{" +
            "id=" + id + '\'' +
            "userId=" + userId + '\'' +
            "roleCode=" + roleCode + '\'' +
            "disabled=" + disabled + '\'' +
            '}';
    }

}
