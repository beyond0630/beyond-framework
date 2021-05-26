package com.beyond.event.driven.subscribe;

import org.springframework.amqp.core.Message;

public interface EventDispatcher {

    void dispatch(Message message) throws Exception;

}
