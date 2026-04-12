package net.cachewrapper.hytale.runner.installer;

import lombok.Getter;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public non-sealed class HttpInstaller extends Installer {

    @Getter
    private static final HttpInstaller instance = new HttpInstaller();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    protected @NotNull CompletableFuture<Void> install(final @NotNull InstallerData installerData) {
        final URI uri = installerData.uri();
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        final Path destinationPath = installerData.destinationPath();
        return this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFile(destinationPath))
                .exceptionally(throwable -> {
                    HytaleRunnerPlugin.LOGGER.error("Ran into a problem in HttpInstaller", throwable);
                    return null;
                })
                .thenAccept(_ -> {});
    }
}
