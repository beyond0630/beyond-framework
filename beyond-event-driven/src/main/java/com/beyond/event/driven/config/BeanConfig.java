package com.beyond.event.driven.config;

import com.beyond.event.driven.common.EventMetadataResolver;
import com.beyond.event.driven.common.EventPublisherOptions;
import com.beyond.event.driven.common.EventSerializer;
import com.beyond.event.driven.common.IdFactory;
import com.beyond.event.driven.common.impl.DefaultEventMetadataResolver;
import com.beyond.event.driven.common.impl.JsonEventSerializer;
import com.beyond.event.driven.common.impl.SnowflakeIdFactory;
import com.beyond.event.driven.publish.EventPublisher;
import com.beyond.event.driven.publish.MessageIdFactory;
import com.beyond.event.driven.publish.MessageOutboxStore;
import com.beyond.event.driven.publish.MessageSender;
import com.beyond.event.driven.publish.impl.*;
import com.beyond.event.driven.service.MessageService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    private static final String APP_NAME = "beyond-event-driven";

    private static final String EXCHANGE = "beyond-event";

    @Bean
    public IdFactory idFactory() {
        return new SnowflakeIdFactory(0, 0);
    }

    @Bean
    public MessageIdFactory messageIdFactory() {
        return new UUIDMessageIdFactory();
    }

    @Bean
    public MessageOutboxStore messageOutboxStore(MessageService messageService) {
        return new DBMessageOutboxStore(messageService);
    }

    @Bean
    public EventSerializer eventSerializer() {
        return new JsonEventSerializer();
    }

    @Bean
    public EventMetadataResolver eventMetadataResolver() {
        return new DefaultEventMetadataResolver(() -> "beyond.eventdriven");
    }

    @Bean
    public EventPublisherOptions eventPublisherOptions() {
        return new EventPublisherOptions() {

            @Override
            public String getAppName() {
                return APP_NAME;
            }

            @Override
            public String getDefaultExchange() {
                return EXCHANGE;
            }
        };
    }

    @Bean
    public MessageSender messageSender(final MessageOutboxStore messageOutboxStore,
                                       final ConnectionFactory connectionFactory) {
        return new RabbitMessageSender(messageOutboxStore, connectionFactory);
    }

    @Bean
    public EventPublisher eventPublisher(final MessageSender messageSender,
                                         final EventSerializer eventSerializer,
                                         final MessageIdFactory messageIdFactory,
                                         final MessageOutboxStore messageOutboxStore,
                                         final EventMetadataResolver eventMetadataResolver,
                                         final EventPublisherOptions eventPublisherOptions) {
        return new TransactionalRabbitEventPublisher(messageSender, eventSerializer, messageIdFactory,
            messageOutboxStore, eventMetadataResolver, eventPublisherOptions);
    }
}
