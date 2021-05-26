package com.beyond.event.driven.common.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.beyond.event.driven.annotation.EventType;
import com.beyond.event.driven.annotation.Namespace;
import com.beyond.event.driven.annotation.Topic;
import com.beyond.event.driven.common.EventMetadata;
import com.beyond.event.driven.option.EventMetadataOptions;
import com.beyond.event.driven.common.EventMetadataResolver;
import com.beyond.event.driven.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class DefaultEventMetadataResolver implements EventMetadataResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventMetadataResolver.class);

    private final Map<Class<?>, EventMetadata> cache;
    private final EventMetadataOptions eventMetadataOptions;

    public DefaultEventMetadataResolver(final EventMetadataOptions eventMetadataOptions) {
        this.cache = new ConcurrentHashMap<>();
        this.eventMetadataOptions = eventMetadataOptions;
    }

    @Override
    public EventMetadata get(final Class<?> clazz) {
        return this.cache.computeIfAbsent(clazz, this::loadEventMetadata);
    }

    private EventMetadata loadEventMetadata(final Class<?> clazz) {
        Assert.notNull(clazz, "Class cat not be null");
        LOGGER.debug("Loading event metadata: {}", clazz.getName());

        // namespace
        final String namespace = Optional.ofNullable(clazz.getAnnotation(Namespace.class))
                .map(Namespace::value)
                .orElseGet(eventMetadataOptions::getNamespace);

        // event type
        final String type = Optional.ofNullable(clazz.getAnnotation(EventType.class))
                .map(EventType::value)
                .orElseGet(() -> NameUtils.camelToSnakeCase(clazz.getSimpleName()));

        final String qualifiedType = NameUtils.combinePrefix(namespace, type);

        final String topic = Optional.ofNullable(clazz.getAnnotation(Topic.class))
                .map(Topic::value)
                .orElse(qualifiedType);
        final DefaultEventMetadata eventMetadata = new DefaultEventMetadata();
        eventMetadata.setJavaClass(clazz);
        eventMetadata.setClassName(clazz.getName());
        eventMetadata.setNamespace(namespace);
        eventMetadata.setType(type);
        eventMetadata.setQualifiedType(qualifiedType);
        eventMetadata.setTopic(topic);
        return eventMetadata;
    }

}
