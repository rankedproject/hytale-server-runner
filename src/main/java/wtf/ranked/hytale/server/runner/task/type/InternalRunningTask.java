package wtf.ranked.hytale.server.runner.task.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import wtf.ranked.hytale.server.runner.task.DefaultRunningTask;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

/**
 * Container for atomic worker tasks used within the boot lifecycle.
 * <p>
 * Tasks extending these classes represent individual "steps" of the
 * process. They are designed to be triggered programmatically by the
 * plugin's internal logic rather than being standalone entry points for the user.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class InternalRunningTask {

    /**
     * An internal worker focused on starting a Java-based process.
     */
    public abstract static class InternalExecRunningTask extends JavaExecRunningTask {

        protected InternalExecRunningTask() {
            setGroup(null);
            setDescription(null);
        }
    }

    /**
     * An internal worker focused on general build-environment logic.
     */
    public abstract static class InternalDefaultRunningTask extends DefaultRunningTask {

        protected InternalDefaultRunningTask() {
            setGroup(null);
            setDescription(null);
        }
    }
}
