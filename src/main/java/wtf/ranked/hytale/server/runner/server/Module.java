package wtf.ranked.hytale.server.runner.server;

import org.gradle.api.Project;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

/**
 * Defines a configuration unit for the server execution environment.
 * <p>
 * Modules encapsulate logic for modifying {@link JavaExecRunningTask} properties,
 * such as classpath, JVM arguments, or working directories, allowing for a
 * pluggable and maintainable launch configuration.
 */
@NullMarked
public interface Module {
    void configure(JavaExecRunningTask runningTask, Project project);
}
