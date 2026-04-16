package net.rankedproject.hytale.boot.resource;

import net.rankedproject.hytale.boot.HytaleBootPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public abstract non-sealed class HttpResourceProvider extends ResourceProvider {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    @Override
    protected @NotNull CompletableFuture<Void> provide(final @NotNull ResourceProvider.ResourceRequest request) {
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(request.uri())
                .GET()
                .timeout(request.timeout())
                .build();

        final File destinationPath = request.destinationFile();
        return this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFile(destinationPath.toPath()))
                .exceptionally(throwable -> {
                    HytaleBootPlugin.LOGGER.error("Ran into a problem in resourceProvider", throwable);
                    return null;
                })
                .thenAccept(_ -> {});
    }
}
