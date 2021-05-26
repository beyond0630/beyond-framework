package org.beyond.longpolling.common;

public interface MessageChannelFactory<R> {

    MessageChannel<R> newChannel(String channelId);

}