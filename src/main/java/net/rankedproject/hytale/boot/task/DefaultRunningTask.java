package net.rankedproject.hytale.boot.task;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

public abstract class DefaultRunningTask extends DefaultTask implements GradleRunningTask {

    @TaskAction
    public void runTask() {
        run();
    }

    @Internal
    protected @NotNull HytaleBootExtension getHytaleBootExtension() {
        return getProject().getExtensions().getByType(HytaleBootExtension.class);
    }
}
