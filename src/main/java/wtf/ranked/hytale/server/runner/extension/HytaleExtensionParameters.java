package wtf.ranked.hytale.server.runner.extension;

import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildServiceParameters;
import org.jspecify.annotations.NonNull;

/**
 * Common parameter definition for Hytale-related Build Services.
 * <p>
 * Since Build Services cannot access the {@code Project} instance directly,
 * this interface provides a standardized way to inject the {@link HytalePluginExtension}
 * configuration into services like downloaders, registries, and resource providers.
 */
public interface HytaleExtensionParameters extends BuildServiceParameters {

    /**
     * Provides access to the global Hytale boot configuration.
     *
     * @return a lazy property containing the boot extension
     */
    @NonNull Property<HytalePluginExtension> getHytalePluginExtension();
}
