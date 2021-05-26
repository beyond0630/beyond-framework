package com.beyond.event.driven.subscribe;

import java.util.List;

public interface EventSubscriptionRegistry {

    List<EventSubscription> getSubscriptions();

    EventSubscription getSubscription(String id);

}
