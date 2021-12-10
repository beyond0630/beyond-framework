package org.beyond.rabc.biz;

/**
 * @author Beyond
 */
public interface TokenStore {

    /**
     * 检查 token
     *
     * @param userId userId
     * @param token  token
     * @return 是否成功
     */
    boolean checkToken(long userId, String token);

    /**
     * 存储 token
     *
     * @param userId userId
     * @param token  token
     */
    void storeToken(long userId, String token);

    /**
     * 删除 token
     *
     * @param userId userId
     */
    void removeToken(long userId);

}
