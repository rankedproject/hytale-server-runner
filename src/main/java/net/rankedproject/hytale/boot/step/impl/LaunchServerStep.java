package net.rankedproject.hytale.boot.step.impl;

import com.google.common.net.HostAndPort;
import lombok.SneakyThrows;
import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.step.TaskStep;
import net.rankedproject.hytale.boot.step.type.TaskStepExec;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * The terminal step that starts the Hytale server process.
 * <p>
 * Configures the JVM classpath, main class, and working directory
 * based on the {@link HytaleBootExtension}.
 */
public abstract class LaunchServerStep extends TaskStepExec {

    @Override
    public final @NotNull TaskStep.Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    @SneakyThrows
    private void startStep() {
        final HytaleBootExtension bootExtension = getHytaleBootExtension();
        final JavaPluginExtension javaExtension = getProject().getExtensions().getByType(JavaPluginExtension.class);
        final SourceSet sourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);

        final ConfigurableFileCollection runClasspath = getProject().getObjects().fileCollection();
        final ConfigurableFileCollection classpath = runClasspath.from(getProject().files(
                bootExtension.getServerJar(),
                sourceSet.getRuntimeClasspath()
        ));

        setClasspath(classpath);
        getMainClass().set(bootExtension.getServerJarMainClass());

        setWorkingDir(bootExtension.getServerDirectory());
        setStandardInput(System.in);

        environment(bootExtension.getEnvironment().get());
        jvmArgs(bootExtension.getJvmArgs().get());

        final InetSocketAddress address = bootExtension.getServerAddress().get();
        final String serverAddress = HostAndPort.fromParts(address.getHostName(), address.getPort()).toString();
        setArgs(List.of(
                "--assets=" + bootExtension.getAssets().get().getAbsolutePath(),
                "--auth-mode=" + bootExtension.getServerOnlineMode().get().getOnlineMode(),
                "-bind=" + serverAddress
        ));
    }
}