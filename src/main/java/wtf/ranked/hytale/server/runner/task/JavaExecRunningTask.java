package wtf.ranked.hytale.server.runner.task;

import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;

/**
 * Base class for tasks that launch the Hytale server process.
 * <p>
 * This class extends {@link JavaExec} to provide a specialized execution lifecycle.
 * It ensures that custom preparation logic is executed via {@link #run()} before
 * the underlying Java process is spawned.
 */
public abstract class JavaExecRunningTask extends JavaExec implements GradleRunningTask {

    /**
     * Executes the task workflow.
     * <p>
     * First invokes the implementation-specific {@link #run()} logic, followed by
     * the standard {@link JavaExec} process execution.
     */
    @TaskAction
    @Override
    public final void exec() {
        run();
        super.exec();
    }

    /**
     * Retrieves the {@link HytalePluginExtension} associated with the current project.
     *
     * @return the plugin extension instance
     */
    @Internal
    @Override
    public final @NonNull HytalePluginExtension getHytalePluginExtension() {
        return getProject().getExtensions().getByType(HytalePluginExtension.class);
    }
}