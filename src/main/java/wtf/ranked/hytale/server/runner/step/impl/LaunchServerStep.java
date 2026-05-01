package wtf.ranked.hytale.server.runner.step.impl;

import com.google.common.net.HostAndPort;
import lombok.SneakyThrows;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.step.type.TaskStepExec;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * The terminal step that starts the Hytale server process.
 * <p>
 * Configures the JVM classpath, main class, and working directory
 * based on the {@link HytalePluginExtension}.
 */
public abstract class LaunchServerStep extends TaskStepExec {

    @SneakyThrows
    @Override
    public void runStep() {
        final HytalePluginExtension pluginExtension = getHytalePluginExtension();
        final JavaPluginExtension javaExtension = getProject().getExtensions().getByType(JavaPluginExtension.class);
        final SourceSet sourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);

        final ConfigurationContainer configurations = getProject().getConfigurations();
        final Configuration runtimeConfig = configurations.getByName(sourceSet.getRuntimeClasspathConfigurationName());

        final FileCollection projectClasses = runtimeConfig.getIncoming().artifactView(view -> {
                    view.componentFilter(ProjectComponentIdentifier.class::isInstance);
                    view.attributes(attributeContainers -> attributeContainers.attribute(
                            LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                            getProject().getObjects().named(LibraryElements.class, LibraryElements.CLASSES)
                    ));
                })
                .getFiles();

        final ConfigurableFileCollection classpath = getProject().getObjects().fileCollection();
        classpath.from(sourceSet.getOutput());
        classpath.from(pluginExtension.getServerJar());
        classpath.from(projectClasses);
        classpath.from(sourceSet.getRuntimeClasspath().minus(runtimeConfig
                .getIncoming()
                .artifactView(view -> view.componentFilter(ProjectComponentIdentifier.class::isInstance))
                .getFiles()));

        getMainClass().set(pluginExtension.getServerJarMainClass());
        setClasspath(classpath);
        setWorkingDir(pluginExtension.getServerDirectory());
        setStandardInput(System.in);

        environment(pluginExtension.getEnvironment().get());
        jvmArgs(pluginExtension.getJvmArgs().get());

        final InetSocketAddress address = pluginExtension.getServerAddress().get();
        final String serverAddress = HostAndPort.fromParts(address.getHostName(), address.getPort()).toString();
        setArgs(List.of(
                "--assets=" + pluginExtension.getAssets().get().getAbsolutePath(),
                "--auth-mode=" + pluginExtension.getServerOnlineMode().get().getOnlineMode(),
                "-bind=" + serverAddress
        ));
    }
}
