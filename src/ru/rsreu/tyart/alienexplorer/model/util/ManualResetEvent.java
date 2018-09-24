package ru.rsreu.tyart.alienexplorer.model.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ManualResetEvent {
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        final int startCount;

        Sync(int count) {
            this.startCount = count;
            setState(startCount);
        }

        public int tryAcquireShared(int acquires) {
            return getState() == 0? 1 : -1;
        }

        public boolean tryReleaseShared(int releases) {
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c-1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }

        void reset() {
            setState(startCount);
        }
    }

    private final Sync sync;

    public ManualResetEvent() {
        this.sync = new Sync(1);
        set();
    }

    public void waitOne() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean waitOne(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void set() {
        sync.releaseShared(1);
    }

    public void reset() {
        sync.reset();
    }
}
