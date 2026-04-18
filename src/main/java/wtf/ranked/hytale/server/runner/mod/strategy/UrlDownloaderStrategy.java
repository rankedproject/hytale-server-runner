package wtf.ranked.hytale.server.runner.mod.strategy;

import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.mod.type.UrlMod;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.resource.exception.ResourceDownloadException;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jspecify.annotations.NonNull;

import java.io.File;

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
    protected final void download(final @NonNull UrlMod mod) throws ResourceDownloadException {
        final HytalePluginExtension hytalePluginExtension = getParameters().getHytalePluginExtension().get();
        final File modsDirectory = hytalePluginExtension.getModDirectory().get().getAsFile();

        final File destinationFile = new File(modsDirectory, mod.getFileName());
        getResourceProvider().get().builder()
                .destinationFile(destinationFile)
                .uri(mod.getUri())
                .timeout(hytalePluginExtension.getDownloadTimeout().get())
                .provide();
    }

    @ServiceReference("httpResourceProvider")
    protected abstract @NonNull Property<HttpResourceProvider> getResourceProvider();
}
