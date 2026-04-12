package net.cachewrapper.hytale.runner.task;

import lombok.RequiredArgsConstructor;
import net.cachewrapper.hytale.runner.step.StepContext;
import net.cachewrapper.hytale.runner.step.impl.InstallFolderStep;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public abstract class RunServerTask extends DefaultTask {

    private final ExecOperations execOperations;

    @TaskAction
    public void run() {
        final Project project = getProject();
        final InstallFolderStep serverInstallStep = new InstallFolderStep(project, this.execOperations);

        final StepContext stepContext = new StepContext(serverInstallStep);
        stepContext.switchNextStep();
    }
}
