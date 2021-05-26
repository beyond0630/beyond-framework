package com.beyond.event.driven.repository;

import com.beyond.event.driven.model.entities.InboxHandled;
import org.springframework.data.repository.CrudRepository;

public interface InboxHandleRepository extends CrudRepository<InboxHandled, String> {
}
