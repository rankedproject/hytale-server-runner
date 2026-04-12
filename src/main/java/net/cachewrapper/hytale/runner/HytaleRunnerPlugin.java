package net.cachewrapper.hytale.runner;

import net.cachewrapper.hytale.runner.task.RunServerTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HytaleRunnerPlugin implements Plugin<@NotNull Project> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HytaleRunnerPlugin.class);
    public static final String HYTALE_SERVER_PATH = "hytaleServer";

    @Override
    public void apply(final @NotNull Project project) {
        final TaskContainer projectTaskContainer = project.getTasks();
        projectTaskContainer.register("runServer", RunServerTask.class);
    }
}
