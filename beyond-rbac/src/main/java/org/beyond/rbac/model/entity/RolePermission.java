package org.beyond.rbac.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 角色权限表
 */
@Entity
@Table(name = "br_role_permission")
@DynamicInsert
@DynamicUpdate
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色编码
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    /**
     * 权限编码
     */
    @Column(name = "permission_code", nullable = false)
    private String permissionCode;

    /**
     * 是否禁用
     */
    @Column(name = "is_disabled", nullable = false)
    private Boolean disabled;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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
     * 权限编码
     */
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    /**
     * 权限编码
     */
    public String getPermissionCode() {
        return permissionCode;
    }

    /**
     * 是否禁用
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * 是否禁用
     */
    public Boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
            "id=" + id + '\'' +
            "roleCode=" + roleCode + '\'' +
            "permissionCode=" + permissionCode + '\'' +
            "disabled=" + disabled + '\'' +
            '}';
    }

}
