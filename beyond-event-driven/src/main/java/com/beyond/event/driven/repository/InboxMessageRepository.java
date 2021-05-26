package com.beyond.event.driven.repository;

import com.beyond.event.driven.model.entities.InboxMessage;
import org.springframework.data.repository.CrudRepository;

public interface InboxMessageRepository extends CrudRepository<InboxMessage, Long> {

    boolean existsByMessageId(String messageId);
}
