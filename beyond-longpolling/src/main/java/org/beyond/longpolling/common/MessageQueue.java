package org.beyond.longpolling.common;

import java.util.List;

public interface MessageQueue {

    void pushMessage(final Message message);

    List<Message> popAllMessages();
}
