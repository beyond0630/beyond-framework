package org.beyond.rbac.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 权限表
 *
 * @author Beyond
 */
@Entity
@Table(name = "br_permission")
@DynamicInsert
@DynamicUpdate
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 权限编码
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * 权限名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 访问地址
     */
    @Column(name = "url", nullable = false)
    private String url;

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
     * 权限编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 权限编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 权限名称
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
        return "Permission{" +
            "id=" + id + '\'' +
            "code=" + code + '\'' +
            "name=" + name + '\'' +
            "disabled=" + disabled + '\'' +
            '}';
    }

    /**
     * 访问地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 访问地址
     */
    public String getUrl() {
        return url;
    }

}
