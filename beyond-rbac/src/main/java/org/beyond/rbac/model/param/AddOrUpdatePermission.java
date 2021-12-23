package org.beyond.rbac.model.param;

/**
 * @author Beyond
 */
public class AddOrUpdatePermission {

    private String code;

    private String name;

    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

}
