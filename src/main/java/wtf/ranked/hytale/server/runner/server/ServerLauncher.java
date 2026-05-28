package wtf.ranked.hytale.server.runner.server;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.task.JavaExecRunningTask;

import java.util.Collection;

/**
 * Orchestrates the server launch sequence using a modular configuration approach.
 * <p>
 * This launcher delegates the complex task of process configuration to a series
 * of {@link Module} implementations, ensuring separation of concerns between
 * classpath setup, JVM arguments, and environment variables.
 */
@NullMarked
@RequiredArgsConstructor
public final class ServerLauncher {

    private final JavaExecRunningTask runningTask;

    public static ServerLauncherBuilder from(final JavaExecRunningTask runningTask) {
        final ServerLauncher launcher = new ServerLauncher(runningTask);
        return new ServerLauncherBuilder(launcher);
    }

    /**
     * Applies all provided modules to the execution task.
     *
     * @param modules a collection of configuration modules to apply
     */
    public void launch(final Collection<Module> modules) {
        modules.forEach(module -> module.configure(runningTask, runningTask.getProject()));
    }
}
