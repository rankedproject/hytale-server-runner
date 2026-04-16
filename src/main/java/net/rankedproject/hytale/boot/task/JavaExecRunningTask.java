package net.rankedproject.hytale.boot.task;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

public abstract class JavaExecRunningTask extends JavaExec implements GradleRunningTask {

    @TaskAction
    @Override
    public final void exec() {
        run();
        super.exec();
    }

    @Internal
    protected @NotNull HytaleBootExtension getHytaleBootExtension() {
        return getProject().getExtensions().getByType(HytaleBootExtension.class);
    }
}
