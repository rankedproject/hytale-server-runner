package net.cachewrapper.hytale.runner.step.impl;

import lombok.RequiredArgsConstructor;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import net.cachewrapper.hytale.runner.step.Step;
import net.cachewrapper.hytale.runner.step.StepContext;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@RequiredArgsConstructor
public final class StartServerStep implements Step {

    private final Project project;
    private final ExecOperations execOperations;

    @Override
    public @NotNull Step.Options options(final @NotNull StepContext stepContext) {
        return Options.builder()
                .startStep(() -> {
                    final File serverDirectory = new File(project.getProjectDir(), HytaleRunnerPlugin.HYTALE_SERVER_PATH);
                    final File startServerFile = new File(serverDirectory, "start.bat");

                    this.execOperations.exec(execSpec -> {
                        execSpec.setExecutable("cmd");
                        execSpec.args("/c", startServerFile.getAbsoluteFile());
                        execSpec.workingDir(serverDirectory);

                        execSpec.setStandardOutput(System.out);
                        execSpec.setErrorOutput(System.err);
                        execSpec.setStandardInput(System.in);
                    });
                })
                .build();
    }
}
