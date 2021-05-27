package com.beyond.event.driven.scheduled;

import com.beyond.event.driven.publish.OutboxRetryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class OutboxMessageRetryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxMessageRetryTask.class);

    private final OutboxRetryManager outboxRetryManager;

    public OutboxMessageRetryTask(final OutboxRetryManager outboxRetryManager) {
        this.outboxRetryManager = outboxRetryManager;
    }

    @Scheduled(fixedDelay = 1000L * 30)
    public void run() {
        LOGGER.debug("Started");
        this.outboxRetryManager.performRetry();
        LOGGER.debug("Finished");
    }
}
