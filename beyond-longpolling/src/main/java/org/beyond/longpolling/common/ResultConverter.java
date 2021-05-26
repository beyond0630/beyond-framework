package org.beyond.longpolling.common;

import java.util.List;

public interface ResultConverter<R> {

    R convert(List<Message> messages);

}
