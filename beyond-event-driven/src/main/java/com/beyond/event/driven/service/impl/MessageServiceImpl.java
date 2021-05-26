package com.beyond.event.driven.service.impl;

import java.util.Date;

import com.beyond.event.driven.common.IdFactory;
import com.beyond.event.driven.model.entities.OutboxMessage;
import com.beyond.event.driven.model.entities.OutboxUnconfirmed;
import com.beyond.event.driven.repository.OutboxMessageRepository;
import com.beyond.event.driven.repository.OutboxUnconfirmedRepository;
import com.beyond.event.driven.service.MessageService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final IdFactory idFactory;
    private final OutboxMessageRepository outboxMessageRepository;
    private final OutboxUnconfirmedRepository outboxUnconfirmedRepository;

    public MessageServiceImpl(final IdFactory idFactory,
                              final OutboxMessageRepository outboxMessageRepository,
                              final OutboxUnconfirmedRepository outboxUnconfirmedRepository) {
        this.idFactory = idFactory;
        this.outboxMessageRepository = outboxMessageRepository;
        this.outboxUnconfirmedRepository = outboxUnconfirmedRepository;
    }

    @Override
    public void addOutboxMessage(final OutboxMessage outboxMessage) {
        final Date now = new Date();
        outboxMessage.setId(idFactory.nextId());
        outboxMessage.setCreatedAt(now);
        this.outboxMessageRepository.save(outboxMessage);

        final OutboxUnconfirmed unconfirmed = new OutboxUnconfirmed();
        unconfirmed.setId(outboxMessage.getMessageId());
        unconfirmed.setRetries(0);
        unconfirmed.setNextRetry(DateUtils.addMinutes(now, 1));
        unconfirmed.setCreatedAt(now);
        unconfirmed.setUpdatedAt(now);
        unconfirmed.setRowVersion(0);
        this.outboxUnconfirmedRepository.save(unconfirmed);
    }

    @Override
    public void deleteOutboxUnconfirmed(final String id) {
        this.outboxUnconfirmedRepository.deleteById(id);
    }

    @Override
    public void updateOutboxRetried(final String id, final int interval) {

    }
}
