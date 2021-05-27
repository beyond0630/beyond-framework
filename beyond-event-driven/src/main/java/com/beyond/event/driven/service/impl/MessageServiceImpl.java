package com.beyond.event.driven.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.beyond.event.driven.common.IdFactory;
import com.beyond.event.driven.model.entities.InboxHandled;
import com.beyond.event.driven.model.entities.InboxMessage;
import com.beyond.event.driven.model.entities.OutboxMessage;
import com.beyond.event.driven.model.entities.OutboxUnconfirmed;
import com.beyond.event.driven.repository.InboxHandledRepository;
import com.beyond.event.driven.repository.InboxMessageRepository;
import com.beyond.event.driven.repository.OutboxMessageRepository;
import com.beyond.event.driven.repository.OutboxUnconfirmedRepository;
import com.beyond.event.driven.service.MessageService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final IdFactory idFactory;
    private final InboxMessageRepository inboxMessageRepository;
    private final InboxHandledRepository inboxHandledRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final OutboxUnconfirmedRepository outboxUnconfirmedRepository;

    public MessageServiceImpl(final IdFactory idFactory,
                              final InboxMessageRepository inboxMessageRepository,
                              final InboxHandledRepository inboxHandledRepository,
                              final OutboxMessageRepository outboxMessageRepository,
                              final OutboxUnconfirmedRepository outboxUnconfirmedRepository) {
        this.idFactory = idFactory;
        this.inboxMessageRepository = inboxMessageRepository;
        this.inboxHandledRepository = inboxHandledRepository;
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

    @Override
    public void addInboxMessage(final InboxMessage message) {
        final Date now =new Date();
        message.setId(this.idFactory.nextId());
        message.setCreatedAt(now);
        this.inboxMessageRepository.save(message);
    }

    @Override
    public boolean isInboxMessageIdExists(final String messageId) {
        return inboxMessageRepository.existsByMessageId(messageId);
    }

    @Override
    public void setInboxMessageHandled(final String key, final String messageId) {
        final InboxHandled record = new InboxHandled();
        record.setId(key);
        record.setMessageId(messageId);
        record.setCreatedAt(new Date());
        this.inboxHandledRepository.save(record);
    }

    @Override
    public boolean isInboxMessageHandled(final String key) {
        return this.inboxHandledRepository.existsById(key);
    }

    @Override
    public List<OutboxMessage> listOutboxUnconfirmed(final int limit) {
        final Pageable page = PageRequest.of(1, limit);
        return this.outboxUnconfirmedRepository.findAllByNextRetryBeforeOrderByNextRetry(new Date(), page)
            .get()
            .map(x -> this.outboxMessageRepository.findByMessageId(x.getId()))
            .collect(Collectors.toList());
    }

}
