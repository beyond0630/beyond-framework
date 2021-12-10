package org.beyond.rabc.biz;

/**
 * @author Beyond
 */
public interface TokenManager {

    /**
     * 验证 token
     *
     * @param token token
     * @return 如果验证成功返回用户 id, 不成功返回 -1;
     */
    long verifyToken(String token);

    /**
     * 创建 token
     *
     * @param userId 用户 Id
     * @return token
     */
    String createToken(long userId);

    /**
     * 清除 token
     *
     * @param userId 用户 id
     */
    void removeToken(long userId);

}
