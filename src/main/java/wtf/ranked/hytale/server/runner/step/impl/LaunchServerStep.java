package wtf.ranked.hytale.server.runner.step.impl;

import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.server.ServerLauncher;
import wtf.ranked.hytale.server.runner.server.impl.ClasspathModule;
import wtf.ranked.hytale.server.runner.server.impl.GradleEnvironmentModule;
import wtf.ranked.hytale.server.runner.server.impl.ServerEnvironmentModule;
import wtf.ranked.hytale.server.runner.step.type.TaskStepExec;

/**
 * The terminal step in the boot lifecycle that launches the Hytale server process.
 * <p>
 * This step orchestrates the server environment configuration by leveraging
 * {@link ServerLauncher}. It applies several modules to set up the JVM classpath,
 * environment variables, and execution parameters defined in the
 * {@link HytalePluginExtension}.
 */
@NullMarked
public abstract class LaunchServerStep extends TaskStepExec {

    @Override
    public void runStep() {
        ServerLauncher.from(this)
                .module(new ClasspathModule())
                .module(new GradleEnvironmentModule())
                .module(new ServerEnvironmentModule())
                .launch();
    }
}