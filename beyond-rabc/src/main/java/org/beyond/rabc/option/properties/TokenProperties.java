package org.beyond.rabc.option.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import org.beyond.rabc.option.TokenOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Beyond
 */
@Component
@ConfigurationProperties(prefix = "application.token")
public class TokenProperties implements TokenOptions {

    private String issuer = "beyond-rabc";
    private String audience = "beyond-rabc";
    private String env = "default";
    private SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
    private String secretKey = "25F410B4D32F277028F60F631C698739";
    private int duration = 60 * 60 * 24 * 7;

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getAudience() {
        return audience;
    }

    @Override
    public String getEnv() {
        return env;
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public int getDuration() {
        return duration;
    }


    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public void setAudience(final String audience) {
        this.audience = audience;
    }

    public void setEnv(final String env) {
        this.env = env;
    }

    public void setAlgorithm(final SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

}
