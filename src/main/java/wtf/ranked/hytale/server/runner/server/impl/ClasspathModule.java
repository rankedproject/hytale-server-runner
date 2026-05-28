package wtf.ranked.hytale.server.runner.server.impl;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.server.Module;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

/**
 * Configures the Java execution classpath for the Hytale server.
 * <p>
 * This module resolves the {@link SourceSet} output, the primary server JAR,
 * and external runtime dependencies. It carefully filters local project
 * components to ensure that classes are correctly loaded into the process classpath.
 */
@NullMarked
public final class ClasspathModule implements Module {

    @Override
    public void configure(final JavaExecRunningTask runningTask, final Project project) {
        final JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
        final HytalePluginExtension pluginExtension = project.getExtensions().getByType(HytalePluginExtension.class);
        final SourceSet sourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);

        final ConfigurationContainer configurations = project.getConfigurations();
        final Configuration runtimeConfig = configurations.getByName(sourceSet.getRuntimeClasspathConfigurationName());

        final FileCollection projectClasses = runtimeConfig.getIncoming().artifactView(view -> {
                    view.componentFilter(ProjectComponentIdentifier.class::isInstance);
                    view.attributes(attributeContainers -> attributeContainers.attribute(
                            LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                            project.getObjects().named(LibraryElements.class, LibraryElements.CLASSES)
                    ));
                })
                .getFiles();

        final ConfigurableFileCollection classpath = project.getObjects().fileCollection();
        classpath.from(sourceSet.getOutput());
        classpath.from(pluginExtension.getServerJar());
        classpath.from(projectClasses);
        classpath.from(sourceSet.getRuntimeClasspath().minus(runtimeConfig
                .getIncoming()
                .artifactView(view -> view.componentFilter(ProjectComponentIdentifier.class::isInstance))
                .getFiles()));

        runningTask.setClasspath(classpath);
    }
}
