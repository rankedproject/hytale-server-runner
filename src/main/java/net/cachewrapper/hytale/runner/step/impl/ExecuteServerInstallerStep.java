package net.cachewrapper.hytale.runner.step.impl;

import lombok.RequiredArgsConstructor;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import net.cachewrapper.hytale.runner.step.Step;
import net.cachewrapper.hytale.runner.step.StepContext;
import net.cachewrapper.hytale.runner.util.ZipUtil;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@RequiredArgsConstructor
public final class ExecuteServerInstallerStep implements Step {

    private final Project project;
    private final ExecOperations execOperations;

    @Override
    public @NotNull Step.Options options(final @NotNull StepContext stepContext) {
        return Options.builder()
                .startStep(() -> {
                    final File serverDirectory = new File(project.getProjectDir(), HytaleRunnerPlugin.HYTALE_SERVER_PATH);
                    final File executableFile = new File(serverDirectory, "hytale-downloader-windows-amd64.exe");

                    final File destinationFile = new File(serverDirectory, "hytale-server.zip");
                    this.execOperations.exec(execSpec -> {
                        execSpec.setExecutable(executableFile.getAbsoluteFile());
                        execSpec.workingDir(serverDirectory);
                        execSpec.args("-download-path", destinationFile.getAbsoluteFile());

                        execSpec.setStandardOutput(System.out);
                        execSpec.setErrorOutput(System.err);
                        execSpec.setStandardInput(System.in);
                    });

                    ZipUtil.unpack(destinationFile, project.getProjectDir());
                    stepContext.switchNextStep();
                })
                .nextStep(new StartServerStep(this.project, this.execOperations))
                .build();
    }
}
