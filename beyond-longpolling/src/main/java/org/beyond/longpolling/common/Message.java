package org.beyond.longpolling.common;

import java.time.LocalDateTime;
import java.util.Map;

public class Message {

    private String id;

    private LocalDateTime publishTime;

    private Map<String, String> headers;

    private String payload;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(final LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", publishTime=" + publishTime +
                ", headers=" + headers +
                ", payload='" + payload + '\'' +
                '}';
    }
}
