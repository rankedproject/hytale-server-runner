package net.rankedproject.hytale.boot.task.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.task.DefaultRunningTask;
import net.rankedproject.hytale.boot.task.JavaExecRunningTask;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class InternalRunningTask {

    public abstract static class InternalExecRunningTask extends JavaExecRunningTask {

        protected InternalExecRunningTask() {
            setGroup(null);
        }
    }

    public abstract static class InternalDefaultRunningTask extends DefaultRunningTask {

        protected InternalDefaultRunningTask() {
            setGroup(null);
        }
    }
}
