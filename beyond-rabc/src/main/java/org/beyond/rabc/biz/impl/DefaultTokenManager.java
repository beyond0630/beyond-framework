package org.beyond.rabc.biz.impl;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.beyond.rabc.biz.TokenManager;
import org.beyond.rabc.common.IdFactory;
import org.beyond.rabc.option.TokenOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Beyond
 */
@Component
public class DefaultTokenManager implements TokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTokenManager.class);

    private static final String CLAIM_APP_ENV = "a.env";
    private static final String CLAIM_USER_ID = "u.id";

    private final IdFactory idFactory;
    private final TokenOptions options;

    public DefaultTokenManager(final IdFactory idFactory,
                               final TokenOptions options) {
        this.idFactory = idFactory;
        this.options = options;
    }

    @Override
    public long verifyToken(final String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parser()
                .setClock(Date::new)
                .setSigningKey(this.options.getSecretKey())
                .requireIssuer(options.getIssuer())
                .requireAudience(options.getAudience())
                .require(CLAIM_APP_ENV, this.options.getEnv())
                .parseClaimsJws(token);
        } catch (JwtException e) {
            LOGGER.debug(e.getMessage(), e);
            return -1;
        }

        Claims body = jws.getBody();
        Date expiration = body.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            return -1;
        }
        return Long.parseLong(body.get(CLAIM_USER_ID, String.class));
    }

    @Override
    public String createToken(final long userId) {
        return Jwts.builder()
            .signWith(this.options.getAlgorithm(), this.options.getSecretKey())
            .setIssuer(options.getIssuer())
            .setAudience(options.getAudience())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + (long) options.getDuration() * 1000L))
            .setId(String.valueOf(idFactory.generate()))
            .claim(CLAIM_APP_ENV, this.options.getEnv())
            .claim(CLAIM_USER_ID, Long.toString(userId))
            .compact();
    }


}
