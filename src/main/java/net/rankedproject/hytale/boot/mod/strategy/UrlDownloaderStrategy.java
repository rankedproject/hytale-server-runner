package net.rankedproject.hytale.boot.mod.strategy;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import net.rankedproject.hytale.boot.resource.HttpResourceProvider;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;

/**
 * Strategy for downloading mods via direct HTTP/HTTPS requests.
 * <p>
 * Retrieves the {@code HttpResourceProvider} service to perform
 * the download and saves the resulting file into the configured
 * Hytale mods directory.
 */
public abstract class UrlDownloaderStrategy extends ModDownloaderStrategy<UrlMod> {

    /**
     * Executes the HTTP download using the metadata provided in the {@link UrlMod}.
     *
     * @param mod the URL-based mod definition
     */
    @Override
    protected void download(final @NotNull UrlMod mod) {
        final HytaleBootExtension hytaleBootExtension = getParameters().getHytaleBootExtension().get();
        final File modsDirectory = hytaleBootExtension.getModDirectory().get().getAsFile();

        final File destinationFile = new File(modsDirectory, mod.getFileName());
        getResourceProvider().get().builder()
                .destinationFile(destinationFile)
                .uri(mod.getUri())
                .timeout(Duration.ofSeconds(15))
                .provide()
                .join();
    }

    @ServiceReference("httpResourceProvider")
    protected abstract @NotNull Property<HttpResourceProvider> getResourceProvider();
}
