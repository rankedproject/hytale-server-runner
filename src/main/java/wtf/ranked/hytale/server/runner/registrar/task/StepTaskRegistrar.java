package wtf.ranked.hytale.server.runner.registrar.task;

import lombok.RequiredArgsConstructor;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.registrar.Registrar;
import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.step.TaskStepRegistry;

/**
 * Registrar implementation responsible for initializing internal lifecycle steps.
 * <p>
 * This class serves as the bridge between the Gradle {@link TaskContainer} and the
 * internal {@link TaskStepRegistry}. When a step is registered:
 * <ol>
 * <li>The task is formally registered in the Gradle build lifecycle.</li>
 * <li>The task's class is associated with its identifier in the {@link TaskStepRegistry}
 * for later resolution by the {@link wtf.ranked.hytale.server.runner.step.TaskStepLoader}.</li>
 * </ol>
 */
@NullMarked
@RequiredArgsConstructor
public final class StepTaskRegistrar implements Registrar<TaskStep> {

    private final Project project;

    @Override
    public void register(final String identifier, final Class<? extends TaskStep> value) {
        final TaskContainer container = project.getTasks();
        container.register(identifier, value);
        TaskStepRegistry.register(identifier, value);
    }
}
