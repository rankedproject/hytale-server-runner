package wtf.ranked.hytale.server.runner.server.impl;

import com.google.common.net.HostAndPort;
import org.gradle.api.Project;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.server.Module;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Configures server-specific command-line arguments and environment variables.
 * <p>
 * Maps properties from {@link HytalePluginExtension} to CLI flags required by the Hytale server
 */
@NullMarked
public final class ServerEnvironmentModule implements Module {

    @Override
    public void configure(JavaExecRunningTask runningTask, Project project) {
        final HytalePluginExtension pluginExtension = project.getExtensions().getByType(HytalePluginExtension.class);

        final InetSocketAddress address = pluginExtension.getServerAddress().get();
        final String serverAddress = HostAndPort.fromParts(address.getHostName(), address.getPort()).toString();

        runningTask.getMainClass().set(pluginExtension.getServerJarMainClass());
        runningTask.environment(pluginExtension.getServerEnvironment().get());

        runningTask.setArgs(List.of(
                "--assets=" + pluginExtension.getAssets().get().getAsFile().getAbsolutePath(),
                "--auth-mode=" + pluginExtension.getServerOnlineMode().get().getIdentifier(),
                "-bind=" + serverAddress
        ));
    }
}