package wtf.ranked.hytale.server.runner.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;

/**
 * Base class for tasks running entirely within the Gradle process.
 * <p>
 * Serves as the foundation for <b>Internal</b> worker tasks or
 * <b>Global</b> tasks that handle file management, updates, or
 * environment setup without booting a server.
 */
public abstract class DefaultRunningTask extends DefaultTask implements GradleRunningTask {

    /**
     * Entry point for Gradle execution.
     */
    @TaskAction
    public final void runTask() {
        run();
    }

    /**
     * {@inheritDoc}
     */
    @Internal
    @Override
    public final @NonNull HytalePluginExtension getHytalePluginExtension() {
        return getProject().getExtensions().getByType(HytalePluginExtension.class);
    }
}
