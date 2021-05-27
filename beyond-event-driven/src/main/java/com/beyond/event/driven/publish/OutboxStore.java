package com.beyond.event.driven.publish;


import java.util.List;

import org.springframework.amqp.core.Message;

public interface OutboxStore {

    /**
     * 保存消息
     *
     * @param message 消息
     */
    void save(Message message);

    /**
     * 删除已确认的消息
     *
     * @param id 消息ID
     */
    void deleteConfirmedMessage(String id);

    List<Message> listUnconfirmed(int limit);

    /**
     * 更新重试信息
     *
     * @param id       id
     * @param interval 间隔
     */
    void updateOutboxRetried(String id, int interval);

}
