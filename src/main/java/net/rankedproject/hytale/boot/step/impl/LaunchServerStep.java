package net.rankedproject.hytale.boot.step.impl;

import lombok.SneakyThrows;
import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.type.StepExec;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class LaunchServerStep extends StepExec {

    @Override
    public @NotNull Step.Options options() {
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

        getMainClass().set(bootExtension.getServerJarMainClass());
        setClasspath(classpath);

        setWorkingDir(bootExtension.getServerDirectory());
        setStandardInput(System.in);

        setArgs(List.of("--assets=" + bootExtension.getAssets().get().getAbsolutePath()));
        jvmArgs(bootExtension.getJvmArgs().getOrElse(new ArrayList<>()));
    }
}