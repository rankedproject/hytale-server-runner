package wtf.ranked.hytale.server.runner.task;

import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;

/**
 * Base class for tasks that launch the Hytale server process.
 * * <p>Typically used as a <b>Global Task</b>. It ensures that all
 * preparation logic in {@link #run()} is completed before the
 * external Java process is started.
 */
public abstract class JavaExecRunningTask extends JavaExec implements GradleRunningTask {

    /**
     * Entry point for Gradle execution.
     * <p>
     * Runs the local task logic first, then calls the superclass
     * to spawn the Java Virtual Machine.
     */
    @TaskAction
    @Override
    public final void exec() {
        run();
        super.exec();
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
