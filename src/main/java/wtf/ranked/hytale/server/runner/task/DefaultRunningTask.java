package wtf.ranked.hytale.server.runner.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;

/**
 * Base class for tasks running entirely within the Gradle process.
 * <p>
 * This class serves as the foundation for both <b>Internal</b> worker tasks
 * and <b>Global</b> orchestrators that perform operations like file management,
 * asset updates, or environment configuration without spawning external processes.
 */
public abstract class DefaultRunningTask extends DefaultTask implements GradleRunningTask {

    /**
     * Entry point for Gradle task execution.
     * <p>
     * Triggers the implementation-specific business logic defined in {@link #run()}.
     */
    @TaskAction
    public final void runTask() {
        run();
    }

    /**
     * Returns the {@link HytalePluginExtension} configured for this project.
     *
     * @return the active configuration extension
     */
    @Internal
    @Override
    public final @NonNull HytalePluginExtension getHytalePluginExtension() {
        return getProject().getExtensions().getByType(HytalePluginExtension.class);
    }
}