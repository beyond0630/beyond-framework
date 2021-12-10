package org.beyond.rabc.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 角色表
 */
@Entity
@Table(name = "br_role")
@DynamicInsert
@DynamicUpdate
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 角色编码
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 是否禁用
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
     * 角色编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 角色编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 角色名称
     */
    public String getName() {
        return name;
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
        return "Role{" +
            "id=" + id + '\'' +
            "code=" + code + '\'' +
            "name=" + name + '\'' +
            "disabled=" + disabled + '\'' +
            '}';
    }

}
