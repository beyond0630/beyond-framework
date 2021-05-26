package com.beyond.event.driven.repository;

import com.beyond.event.driven.model.entities.OutboxMessage;
import org.springframework.data.repository.CrudRepository;

public interface OutboxMessageRepository extends CrudRepository<OutboxMessage, Long> {
}
