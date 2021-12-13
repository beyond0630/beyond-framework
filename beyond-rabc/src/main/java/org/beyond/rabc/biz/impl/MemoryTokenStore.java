package org.beyond.rabc.biz.impl;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.lang.Assert;
import org.beyond.rabc.biz.TokenStore;

/**
 * @author Beyond
 */
//@Component
@Deprecated
public class MemoryTokenStore implements TokenStore {

    private static final Map<Long, String> STORE = new HashMap<>(1024);

    @Override
    public boolean checkToken(final long userId, final String token) {
        Assert.hasText(token, "parameter token can not be null");
        String storeToken = STORE.get(userId);
        return token.equals(storeToken);
    }

    @Override
    public void storeToken(final long userId, final String token) {
        STORE.put(userId, token);
    }

    @Override
    public void removeToken(final long userId) {
        STORE.remove(userId);
    }

}
