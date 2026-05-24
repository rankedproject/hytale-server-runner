package wtf.ranked.hytale.server.runner.step;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tools.ant.TaskContainer;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Global registry for mapping {@link TaskStep} implementations to their unique Gradle task identifiers.
 * <p>
 * This registry acts as the central lookup service for the {@link TaskStepLoader}, ensuring
 * that internal lifecycle steps—such as those involved in server asset preparation or
 * mod downloading—can be resolved consistently across the plugin's execution pipeline.
 * <p>
 * It is used to associate step classes with the specific task names registered in the
 * {@link TaskContainer}, facilitating the dynamic linking of
 * dependencies within the {@code hytale runner lifecycle} task group.
 * * @see TaskStepLoader
 */
@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskStepRegistry {

    private static final Map<Class<? extends TaskStep>, String> TASK_STEP_NAME = new HashMap<>();

    public static void register(final String name, final Class<? extends TaskStep> taskStep) {
        TASK_STEP_NAME.put(taskStep, name);
    }

    public static String getName(final Class<? extends TaskStep> taskStep) {
        return TASK_STEP_NAME.get(taskStep);
    }
}
