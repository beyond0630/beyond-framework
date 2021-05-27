package com.beyond.event.driven.repository;

import java.util.Date;

import com.beyond.event.driven.model.entities.OutboxUnconfirmed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OutboxUnconfirmedRepository extends PagingAndSortingRepository<OutboxUnconfirmed, String> {

    Page<OutboxUnconfirmed> findAllByNextRetryBeforeOrderByNextRetry(final Date nextRetry, final Pageable pageable);

}
