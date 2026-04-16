package net.rankedproject.hytale.boot.resource;

import net.rankedproject.hytale.boot.HytaleBootPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP implementation of the ResourceProvider.
 * <p>
 * Uses the native Java {@link HttpClient} to perform
 * asynchronous file downloads with redirect support and custom timeouts.
 */
public abstract non-sealed class HttpResourceProvider extends ResourceProvider {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    /**
     * Executes an HTTP GET request and streams the response directly to a file.
     *
     * @param request the URI and destination file details
     * @return a future indicating completion or failure
     */
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
