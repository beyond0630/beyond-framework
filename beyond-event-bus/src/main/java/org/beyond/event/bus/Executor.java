package org.beyond.event.bus;

public interface Executor {

    void execute(Runnable runnable);
}
