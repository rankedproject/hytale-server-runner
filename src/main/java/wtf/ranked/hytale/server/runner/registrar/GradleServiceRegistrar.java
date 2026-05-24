package wtf.ranked.hytale.server.runner.registrar;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceRegistry;
import org.gradle.api.services.BuildServiceSpec;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.extension.HytaleExtensionParameters;

/**
 * Registrar responsible for configuring and providing Gradle Shared Build Services.
 * <p>
 * Shared services are used to manage global state or heavy resources across
 * parallel tasks. This registrar ensures that any registered service is
 * automatically injected with the current {@link HytalePluginExtension}
 * configuration.
 */
@NullMarked
public final class GradleServiceRegistrar implements Registrar<BuildService<HytaleExtensionParameters>> {

    private final Project project;
    private final BuildServiceRegistry serviceRegistry;

    public GradleServiceRegistrar(final Project project) {
        this.project = project;
        this.serviceRegistry = project.getGradle().getSharedServices();
    }

    /**
     * Registers a build service if it is not already present in the registry.
     * <p>
     * The service is configured with a {@link Action} that
     * wires the project's Hytale boot extension into the service's parameters.
     *
     * @param identifier   the unique name for the shared service
     * @param value the class implementation of the build service
     */
    @Override
    public void register(
            final String identifier,
            final Class<? extends BuildService<HytaleExtensionParameters>> value
    ) {
        this.serviceRegistry.registerIfAbsent(identifier, value, buildServiceSpec());
    }

    private Action<? super BuildServiceSpec<HytaleExtensionParameters>> buildServiceSpec() {
        return serviceSpec -> {
            final HytalePluginExtension pluginExtension = project.getExtensions().findByType(HytalePluginExtension.class);
            serviceSpec.getParameters().getHytalePluginExtension().set(pluginExtension);
        };
    }
}
