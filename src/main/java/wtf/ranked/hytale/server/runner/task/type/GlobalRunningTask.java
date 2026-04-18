package wtf.ranked.hytale.server.runner.task.type;

import wtf.ranked.hytale.server.runner.HytaleServerRunnerPlugin;
import wtf.ranked.hytale.server.runner.step.TaskStep;
import org.gradle.api.DefaultTask;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Orchestrator task that provides a high-level entry point for users.
 * <p>
 * Global tasks are organized under the {@code hytale} group and
 * coordinate the execution of multiple {@link TaskStep} implementations
 * to perform complex operations like launching or updating the server.
 */
public abstract class GlobalRunningTask extends DefaultTask {

    /**
     * Initializes the task and assigns it to the plugin's task group.
     */
    protected GlobalRunningTask() {
        setGroup(HytaleServerRunnerPlugin.PLUGIN_GROUP);
    }

    /**
     * Defines the ordered sequence of steps required for this task's lifecycle.
     *
     * @return a list of step classes to be executed in order
     */
    public abstract @NonNull List<Class<? extends TaskStep>> steps();
}
