package com.beyond.event.driven.utils;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.digest.DigestUtils;


public final class ConsumeKeyUtils {

    private ConsumeKeyUtils() {
        throw new UnsupportedOperationException();
    }

    public static String compute(final String queue, final String messageId) {
        String data = queue + ':' + messageId;
        return DigestUtils.sha1Hex(data.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

}
