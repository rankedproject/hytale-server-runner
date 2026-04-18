package wtf.ranked.hytale.server.runner.step.impl;

import com.google.common.net.HostAndPort;
import lombok.SneakyThrows;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.step.type.TaskStepExec;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.jspecify.annotations.NonNull;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * The terminal step that starts the Hytale server process.
 * <p>
 * Configures the JVM classpath, main class, and working directory
 * based on the {@link HytalePluginExtension}.
 */
public abstract class LaunchServerStep extends TaskStepExec {

    @Override
    public final @NonNull Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    @SneakyThrows
    private void startStep() {
        final HytalePluginExtension PluginExtension = getHytalePluginExtension();
        final JavaPluginExtension javaExtension = getProject().getExtensions().getByType(JavaPluginExtension.class);
        final SourceSet sourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);

        final ConfigurableFileCollection runClasspath = getProject().getObjects().fileCollection();
        final ConfigurableFileCollection classpath = runClasspath.from(getProject().files(
                PluginExtension.getServerJar(),
                sourceSet.getRuntimeClasspath()
        ));

        setClasspath(classpath);
        getMainClass().set(PluginExtension.getServerJarMainClass());

        setWorkingDir(PluginExtension.getServerDirectory());
        setStandardInput(System.in);

        environment(PluginExtension.getEnvironment().get());
        jvmArgs(PluginExtension.getJvmArgs().get());

        final InetSocketAddress address = PluginExtension.getServerAddress().get();
        final String serverAddress = HostAndPort.fromParts(address.getHostName(), address.getPort()).toString();
        setArgs(List.of(
                "--assets=" + PluginExtension.getAssets().get().getAbsolutePath(),
                "--auth-mode=" + PluginExtension.getServerOnlineMode().get().getOnlineMode(),
                "-bind=" + serverAddress
        ));
    }
}
