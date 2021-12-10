package org.beyond.rabc.option;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 配置
 *
 * @author Beyond
 */
public interface TokenOptions {

    String getIssuer();

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
