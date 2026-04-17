package net.rankedproject.hytale.boot.registrar;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.extension.HytaleExtensionParameters;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceRegistry;
import org.gradle.api.services.BuildServiceSpec;
import org.jspecify.annotations.NonNull;

/**
 * Registrar responsible for configuring and providing Gradle Shared Build Services.
 * <p>
 * Shared services are used to manage global state or heavy resources across
 * parallel tasks. This registrar ensures that any registered service is
 * automatically injected with the current {@link HytaleBootExtension}
 * configuration.
 */
public final class GradleServiceRegistrar implements Registrar<BuildService<HytaleExtensionParameters>> {

    private final Project project;
    private final BuildServiceRegistry serviceRegistry;

    public GradleServiceRegistrar(final @NonNull Project project) {
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
     * @param buildService the class implementation of the build service
     */
    @Override
    public void register(
            final @NonNull String identifier,
            final @NonNull Class<? extends BuildService<HytaleExtensionParameters>> buildService
    ) {
        this.serviceRegistry.registerIfAbsent(identifier, buildService, buildServiceSpec());
    }

    private @NonNull Action<? super BuildServiceSpec<HytaleExtensionParameters>> buildServiceSpec() {
        return serviceSpec -> {
            final HytaleBootExtension bootExtension = project.getExtensions().findByType(HytaleBootExtension.class);
            serviceSpec.getParameters().getHytaleBootExtension().set(bootExtension);
        };
    }
}
