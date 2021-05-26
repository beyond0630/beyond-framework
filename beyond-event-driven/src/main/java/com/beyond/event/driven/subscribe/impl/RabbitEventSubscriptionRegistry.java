package com.beyond.event.driven.subscribe.impl;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import com.beyond.event.driven.annotation.Subscription;
import com.beyond.event.driven.common.EventMetadata;
import com.beyond.event.driven.common.EventMetadataResolver;
import com.beyond.event.driven.option.EventSubscriptionOptions;
import com.beyond.event.driven.subscribe.EventHandler;
import com.beyond.event.driven.subscribe.EventHandlerInstanceExporter;
import com.beyond.event.driven.subscribe.EventSubscription;
import com.beyond.event.driven.subscribe.EventSubscriptionRegistry;
import com.beyond.event.driven.utils.NameUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;

public class RabbitEventSubscriptionRegistry implements EventSubscriptionRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitEventSubscriptionRegistry.class);
    private static final String HANDLER_METHOD_NAME = "handle";

    private final Map<String, EventSubscription> registry = new HashMap<>();
    private final EventMetadataResolver eventMetadataResolver;
    private final EventSubscriptionOptions eventSubscriptionOptions;

    public RabbitEventSubscriptionRegistry(final EventMetadataResolver eventMetadataResolver,
                                           final EventSubscriptionOptions eventSubscriptionOptions,
                                           final EventHandlerInstanceExporter handlerInstanceExporter) {
        this.eventMetadataResolver = eventMetadataResolver;
        this.eventSubscriptionOptions = eventSubscriptionOptions;
        this.loadEventSubscriptions(handlerInstanceExporter);
    }

    @Override
    public List<EventSubscription> getSubscriptions() {
        return new ArrayList<>(this.registry.values());
    }

    @Override
    public EventSubscription getSubscription(final String id) {
        return this.registry.get(id);
    }

    protected void loadEventSubscriptions(final EventHandlerInstanceExporter handlerInstanceExporter) {
        final List<EventHandler<?>> instances = handlerInstanceExporter.getInstances();
        if (instances == null || instances.isEmpty()) {
            return;
        }
        instances.forEach(this::loadEventSubscription);
    }

    protected void loadEventSubscription(final Object eventHandler) {
        final Class<?> handlerClass = eventHandler.getClass();
        LOGGER.debug("Loading event subscription: {}", handlerClass.getName());
        final ResolvableType interfaceType = this.findHandlerInterface(ResolvableType.forClass(handlerClass));
        if (interfaceType == null) {
            throw new IllegalStateException(handlerClass.getName() + " dose not implement EventHandler interface.");
        }

        // 第一个泛型参数的参数类型 -- event type
        final Class<?> eventClass = interfaceType.getGeneric(0).getRawClass();
        if (eventClass == null) {
            throw new IllegalStateException("Unable to detect generic parameter for " + handlerClass.getName());
        }

        final EventMetadata eventMetadata = this.eventMetadataResolver.get(eventClass);
        if (eventMetadata == null) {
            throw new IllegalStateException("Could not get event metadata for " + eventClass.getName());
        }

        final RabbitEventHandlerMethod handlerMethod = new RabbitEventHandlerMethod();
        handlerMethod.setEventType(eventMetadata.getType());
        handlerMethod.setEventClass(eventClass);
        handlerMethod.setHandlerClass(handlerClass);
        handlerMethod.setHandlerMethod(this.findHandlerMethod(handlerClass, eventClass));
        handlerMethod.setHandlerInstance(eventHandler);

        final Subscription subAnnotation = handlerClass.getAnnotation(Subscription.class);
        final String name = this.getSubscriptionName(handlerClass, subAnnotation);
        final String eventType = handlerMethod.getEventType();
        final String queue = this.getQueueName(name);
        final String subscriptionId = String.format("%s:%s", queue, eventType);

        if (this.registry.containsKey(subscriptionId)) {
            throw new IllegalStateException("Duplicate subscription id: " + subscriptionId);
        }

        this.registry
            .computeIfAbsent(subscriptionId, k -> {
                final RabbitEventSubscription subscription = new RabbitEventSubscription();
                subscription.setId(subscriptionId);
                subscription.setName(name);
                subscription.setQueue(queue);
                subscription.setEventType(eventType);
                subscription.setEventClass(eventClass);
                subscription.setBindingKeys(this.getBindingKeys(eventMetadata.getTopic(), subAnnotation));
                return subscription;
            })
            .addMethod(handlerMethod);
        // 现在不会出现同一个订阅多个 Handler 的情况了，暂时保留原来的用法
    }

    protected List<String> getBindingKeys(final String topic, final Subscription annotation) {
        if (annotation == null || annotation.tags().length <= 0) {
            return Collections.singletonList(topic);
        }

        return Arrays.stream(annotation.tags())
            .map(tag -> NameUtils.combineSuffix(topic, tag))
            .collect(Collectors.toList());
    }

    protected String getSubscriptionName(final Class<?> handlerClass, final Subscription annotation) {
        if (annotation == null || !Strings.isNullOrEmpty(annotation.value())) {
            return NameUtils.camelToSnakeCase(handlerClass.getSimpleName());
        }
        return annotation.value();
    }

    protected String getQueueName(final String subscriptionName) {
        return NameUtils.combineSuffix(this.eventSubscriptionOptions.getQueueNamespace(), subscriptionName);
    }

    protected ResolvableType findHandlerInterface(final ResolvableType handlerType) {
        ResolvableType type = handlerType;
        while (type != ResolvableType.NONE) {
            final ResolvableType[] interfaces = type.getInterfaces();
            for (final ResolvableType iface : interfaces) {
                if (Objects.equals(iface.getRawClass(), EventHandler.class)) {
                    return iface;
                }
            }
            type = type.getSuperType();
        }
        return null;
    }

    protected Method findHandlerMethod(final Class<?> handlerClass, final Class<?> eventClass) {
        try {
            return handlerClass.getMethod(HANDLER_METHOD_NAME, eventClass);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

