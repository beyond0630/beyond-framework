package org.beyond.rbac.option;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 配置
 *
 * @author Beyond
 */
public interface TokenOptions {

    /**
     * 签发人
     *
     * @return string
     */
    String getIssuer();

    /**
     * 受众
     *
     * @return string
     */
    String getAudience();

    /**
     * 环境
     *
     * @return env
     */
    String getEnv();

    /**
     * 加密算法
     *
     * @return algorithm
     */
    SignatureAlgorithm getAlgorithm();

    /**
     * 加密 key
     *
     * @return secret key
     */
    String getSecretKey();

    /**
     * 过期时间
     *
     * @return 过期时间
     */
    int getDuration();

}
