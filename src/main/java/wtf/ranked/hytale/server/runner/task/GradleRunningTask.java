package wtf.ranked.hytale.server.runner.task;

import org.gradle.api.Task;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;

/**
 * Root interface for all Hytale boot-related tasks.
 * <p>
 * Anchors the boot lifecycle by defining how tasks execute logic.
 * This interface supports a two-tier architecture:
 * <ul>
 * <li><b>Internal Tasks:</b> Atomic workers performing specific steps
 * like downloading or unpacking.</li>
 * <li><b>Global Tasks:</b> Orchestrators that link multiple Internal
 * tasks into a sequential execution queue.</li>
 * </ul>
 */
public interface GradleRunningTask extends Task {

    /**
     * Executes the specific business logic for this task.
     */
    void run();

    /**
     * Provides access to the Hytale configuration settings.
     *
     * @return the active configuration extension
     */
    @NonNull HytalePluginExtension getHytalePluginExtension();
}
