package org.beyond.event.bus.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.beyond.event.bus.Register;
import org.beyond.event.bus.SubscribeObject;
import org.beyond.event.bus.Subscriber;
import org.beyond.event.bus.SubscriberFactory;
import org.beyond.event.bus.annotation.Subscribe;

public class DefaultRegister implements Register {

    private final Map<String, ConcurrentLinkedQueue<Subscriber>> subscriberContainer = new ConcurrentHashMap<>();

    private final SubscriberFactory subscriberFactory;

    public DefaultRegister(final SubscriberFactory subscriberFactory) {this.subscriberFactory = subscriberFactory;}

    @Override
    public void bind(final SubscribeObject subscriber) {
        this.getSubscribeMethods(subscriber)
                .forEach(m -> tierSubscriber(subscriber, m));
    }

    @Override
    public void unbind(final SubscribeObject subscriber) {
        subscriberContainer
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(x -> x.getSubscribeObject() == subscriber)
                .forEach(Subscriber::disable);
    }

    @Override
    public ConcurrentLinkedQueue<Subscriber> scanSubscribe(final String topic) {
        return subscriberContainer.get(topic);
    }

    private void tierSubscriber(final SubscribeObject subscriber, final Method method) {
        final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
        final String topic = subscribe.topic();
        subscriberContainer.computeIfAbsent(topic, x -> new ConcurrentLinkedQueue<>());
        subscriberContainer.get(topic).add(this.subscriberFactory.createSubscriber(subscriber, method));
    }

    private List<Method> getSubscribeMethods(Object subscriber) {
        final List<Method> methods = new ArrayList<>();
        Class<?> temp = subscriber.getClass();
        while (temp != null) {
            final Method[] declaredMethods = temp.getDeclaredMethods();
            Arrays.stream(declaredMethods)
                    .filter(m -> m.isAnnotationPresent(Subscribe.class)
                            && m.getParameterCount() == 1 &&
                            m.getModifiers() == Modifier.PUBLIC)
                    .forEach(methods::add);
            temp = temp.getSuperclass();
        }
        return methods;
    }
}
