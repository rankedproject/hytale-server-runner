package wtf.ranked.hytale.server.runner.task.type;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.HytaleServerRunnerPlugin;
import wtf.ranked.hytale.server.runner.step.TaskStep;

import java.util.List;

/**
 * Base class for high-level orchestrator tasks.
 * <p>
 * Global tasks serve as the primary entry points for users. They are categorized
 * under the {@link HytaleServerRunnerPlugin#GLOBAL_TASK_GROUP} and are responsible
 * for coordinating the execution flow by aggregating multiple {@link TaskStep}
 * implementations into a cohesive lifecycle.
 */
public abstract class GlobalRunningTask extends DefaultTask {

    /**
     * Initializes the task and assigns it to the global plugin task group.
     */
    protected GlobalRunningTask() {
        setGroup(HytaleServerRunnerPlugin.GLOBAL_TASK_GROUP);
    }

    /**
     * Defines the ordered sequence of {@link TaskStep} implementations that
     * constitute the task's execution lifecycle.
     *
     * @return an ordered list of step classes to be executed
     */
    public abstract @NonNull List<Class<? extends TaskStep>> steps();

    /**
     * Retrieves the {@link HytalePluginExtension} configured for this project.
     *
     * @return the active configuration extension
     */
    @Internal
    protected @NonNull HytalePluginExtension getHytalePluginExtension() {
        return getProject().getExtensions().getByType(HytalePluginExtension.class);
    }
}