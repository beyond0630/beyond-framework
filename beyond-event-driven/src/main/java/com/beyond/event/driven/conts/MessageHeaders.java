package com.beyond.event.driven.conts;

public final class MessageHeaders {

    public static final String X_ORIGIN_EXCHANGE = "x-origin-exchange";
    public static final String X_ORIGIN_ROUTING_KEY = "x-origin-routing-key";
    public static final String X_APP_NAME = "x-app-name";
    public static final String X_JAVA_CLASS = "x-java-class";

    private MessageHeaders() {
        throw new UnsupportedOperationException();
    }

}
