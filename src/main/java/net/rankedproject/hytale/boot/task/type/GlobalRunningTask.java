package net.rankedproject.hytale.boot.task.type;

import net.rankedproject.hytale.boot.HytaleBootPlugin;
import net.rankedproject.hytale.boot.step.Step;
import org.gradle.api.DefaultTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Orchestrator task that provides a high-level entry point for users.
 * <p>
 * Global tasks are organized under the {@code hytale} group and
 * coordinate the execution of multiple {@link Step} implementations
 * to perform complex operations like launching or updating the server.
 */
public abstract class GlobalRunningTask extends DefaultTask {

    /**
     * Initializes the task and assigns it to the plugin's task group.
     */
    protected GlobalRunningTask() {
        setGroup(HytaleBootPlugin.PLUGIN_GROUP);
    }

    /**
     * Defines the ordered sequence of steps required for this task's lifecycle.
     *
     * @return a list of step classes to be executed in order
     */
    public abstract @NotNull List<Class<? extends Step>> steps();
}