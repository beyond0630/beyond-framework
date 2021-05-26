package com.beyond.event.driven.repository;

import com.beyond.event.driven.model.entities.OutboxUnconfirmed;
import org.springframework.data.repository.CrudRepository;

public interface OutboxUnconfirmedRepository extends CrudRepository<OutboxUnconfirmed, String> {
}
