package org.beyond.rabc.biz;

/**
 * @author Beyond
 */
public interface AccessManager {

    /**
     * 检查用户是有权限
     *
     * @param userId        userId
     * @param permissionUrl 权限 url
     * @return boolean
     */
    boolean userHasPermission(long userId, String permissionUrl);

}
