package wtf.ranked.hytale.server.runner.server.impl;

import org.gradle.api.Project;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.server.Module;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

/**
 * Sets basic Gradle and JVM environment properties for the server.
 * <p>
 * Configures the working directory, JVM heap/args, and pipes standard
 * system input to the server process for interactive management.
 */
@NullMarked
public final class GradleEnvironmentModule implements Module {

    @Override
    public void configure(final JavaExecRunningTask runningTask, final Project project) {
        final HytalePluginExtension pluginExtension = project.getExtensions().getByType(HytalePluginExtension.class);

        runningTask.setWorkingDir(pluginExtension.getServerDirectory());
        runningTask.jvmArgs(pluginExtension.getServerJvmArgs().get());
        runningTask.setStandardInput(System.in);
    }
}
