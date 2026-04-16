package net.rankedproject.hytale.boot.extension;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildServiceParameters;
import org.jetbrains.annotations.NotNull;

/**
 * Common parameter definition for Hytale-related Build Services.
 * <p>
 * Since Build Services cannot access the {@code Project} instance directly,
 * this interface provides a standardized way to inject the {@link HytaleBootExtension}
 * configuration into services like downloaders, registries, and resource providers.
 */
public interface HytaleExtensionParameters extends BuildServiceParameters {

    /**
     * Provides access to the global Hytale boot configuration.
     *
     * @return a lazy property containing the boot extension
     */
    @NotNull Property<HytaleBootExtension> getHytaleBootExtension();
}