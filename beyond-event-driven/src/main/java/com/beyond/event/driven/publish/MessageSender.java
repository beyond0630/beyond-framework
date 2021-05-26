package com.beyond.event.driven.publish;

import org.springframework.amqp.core.Message;

public interface MessageSender {

    void send(String exchange, String routingKey, Message message);
}
