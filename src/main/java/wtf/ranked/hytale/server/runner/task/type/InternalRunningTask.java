package wtf.ranked.hytale.server.runner.task.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import wtf.ranked.hytale.server.runner.HytaleServerRunnerPlugin;
import wtf.ranked.hytale.server.runner.task.DefaultRunningTask;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

/**
 * Container for atomic worker tasks used within the Hytale server boot lifecycle.
 * <p>
 * This class serves as a namespace for specialized base tasks. Tasks extending
 * these classes represent individual "steps" of the process (e.g., downloading
 * files, preparing directories).
 * <p>
 * These tasks are intended to be triggered programmatically by the plugin's
 * internal orchestration logic rather than serving as standalone entry points for end-users.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class InternalRunningTask {

    /**
     * Base class for internal tasks that require spawning a separate Java process.
     * <p>
     * Automatically assigns the task to the {@link HytaleServerRunnerPlugin#INTERNAL_TASK_GROUP}
     * and hides it from the default Gradle task list by setting the description to {@code null}.
     */
    public abstract static class InternalExecRunningTask extends JavaExecRunningTask {

        protected InternalExecRunningTask() {
            setGroup(HytaleServerRunnerPlugin.INTERNAL_TASK_GROUP);
            setDescription(null);
        }
    }

    /**
     * Base class for internal tasks that perform operations within the current JVM.
     * <p>
     * Typically used for file system operations or configuration validation.
     * Automatically assigns the task to the {@link HytaleServerRunnerPlugin#INTERNAL_TASK_GROUP}
     * and hides it from the default Gradle task list.
     */
    public abstract static class InternalDefaultRunningTask extends DefaultRunningTask {

        protected InternalDefaultRunningTask() {
            setGroup(HytaleServerRunnerPlugin.INTERNAL_TASK_GROUP);
            setDescription(null);
        }
    }
}