package org.beyond.event.bus.impl;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.beyond.event.bus.*;

public class DefaultDispatcher implements Dispatcher {

    private final Executor executor;
    private final EventExceptionHandler eventExceptionHandler;

    public DefaultDispatcher(final Executor executor, final EventExceptionHandler eventExceptionHandler) {
        this.executor = Optional.ofNullable(executor).orElseGet(SeqExecutor::new);
        this.eventExceptionHandler = Optional.ofNullable(eventExceptionHandler).orElseGet(DefaultEventExceptionHandler::new);;
    }

    @Override
    public void dispatch(final EventBus eventBus, final Register register, final Event event, final String topic) {
        final ConcurrentLinkedQueue<Subscriber> subscribers = register.scanSubscribe(topic);
        if (subscribers == null) {
            eventExceptionHandler.handler(new IllegalArgumentException("The topic "+ topic +" not bind yet"),
                    new DefaultEventContext(eventBus.getBusName(), null, event));
            return;
        }

        subscribers.stream()
                .filter(Subscriber::isEnable)
                .filter(x -> {
                    final Method method = x.getSubscribeMethod();
                    final Class<?> aClass = method.getParameterTypes()[0];
                    return aClass.isAssignableFrom(event.getClass());
                })
                .forEach(x -> realInvokeSubscribe(x, event, eventBus));
    }

    private void realInvokeSubscribe(final Subscriber subscriber, final Event event, final EventBus eventBus) {
        final Object subscribeObject = subscriber.getSubscribeObject();
        final Method method = subscriber.getSubscribeMethod();
        executor.execute(() -> {
            try {
                method.invoke(subscribeObject, event);
            } catch (Exception e) {
                if (Objects.nonNull(eventExceptionHandler)) {
                    eventExceptionHandler.handler(e,
                            new DefaultEventContext(eventBus.getBusName(), null, event));
                }
            }
        });

    }

    @Override
    public void close() {
        if (executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdown();
        }
    }
}
