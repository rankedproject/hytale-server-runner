package net.cachewrapper.hytale.runner.step.impl;

import lombok.RequiredArgsConstructor;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import net.cachewrapper.hytale.runner.installer.HttpInstaller;
import net.cachewrapper.hytale.runner.step.Step;
import net.cachewrapper.hytale.runner.step.StepContext;
import net.cachewrapper.hytale.runner.util.ZipUtil;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
public final class InstallFolderStep implements Step {

    private static final String HYTALE_SERVER_LINK = "https://downloader.hytale.com/hytale-downloader.zip";
    private static final List<String> FILES = List.of(
            "HytaleServer.jar",
            "Asset.jar"
    );

    private final Project project;
    private final ExecOperations execOperations;

    @Override
    public @NotNull Step.Options options(final @NotNull StepContext stepContext) {
        return Options.builder()
                .startStep(() -> {
                    final File projectDirectory = project.getProjectDir();
                    final File serverDirectory = new File(projectDirectory, HytaleRunnerPlugin.HYTALE_SERVER_PATH);

                    if (!serverDirectory.exists()) {
                        serverDirectory.mkdirs();
                    }

                    final boolean containAllFiles = FILES.stream()
                            .map(file -> new File(serverDirectory, file))
                            .allMatch(File::exists);
                    if (containAllFiles) {
                        stepContext.switchNextStep();
                        return;
                    }

                    final File destinationFile = new File(serverDirectory, "hytale-downloader.zip");
                    HttpInstaller.getInstance().builder()
                            .uri(HYTALE_SERVER_LINK)
                            .timeout(Duration.ofSeconds(25))
                            .destinationPath(destinationFile.toPath())
                            .install()
                            .thenRun(() -> {
                                ZipUtil.unpack(destinationFile, projectDirectory);
                                stepContext.switchNextStep();
                            })
                            .join();
                })
                .nextStep(new ExecuteServerInstallerStep(this.project, this.execOperations))
                .build();
    }
}
