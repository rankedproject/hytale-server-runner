package net.rankedproject.hytale.boot.mod.strategy;

import net.rankedproject.hytale.boot.mod.type.UrlMod;
import net.rankedproject.hytale.boot.resource.HttpResourceProvider;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;

public abstract class UrlDownloaderStrategy extends ModDownloaderStrategy<UrlMod> {

    @Override
    protected void download(final @NotNull UrlMod mod) {
        final File modsDirectory = getParameters().getModDirectory().get().getAsFile();
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
