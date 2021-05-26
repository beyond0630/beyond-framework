package org.beyond.event.bus.impl;

import org.beyond.event.bus.Executor;

public class SeqExecutor implements Executor {

    @Override
    public void execute(final Runnable runnable) {
        runnable.run();
    }
}
