package com.beyond.event.driven.common.impl;

import com.beyond.event.driven.common.Event;
import com.beyond.event.driven.common.EventSerializer;
import com.beyond.event.driven.utils.JsonUtils;
import org.springframework.amqp.core.MessageProperties;

public class JsonEventSerializer implements EventSerializer {

    @Override
    public String getContentType() {
        return MessageProperties.CONTENT_TYPE_JSON;
    }

    @Override
    public byte[] serialize(final Event event) {
        return JsonUtils.serializeAsBytes(event);
    }

    @Override
    public <T extends Event> T deserialize(final byte[] data, final Class<T> clazz) {
        return JsonUtils.deserialize(data, clazz);
    }
}
