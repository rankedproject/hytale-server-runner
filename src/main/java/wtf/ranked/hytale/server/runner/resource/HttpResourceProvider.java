package wtf.ranked.hytale.server.runner.resource;

import io.vavr.control.Try;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.resource.exception.ResourceDownloadException;
import wtf.ranked.hytale.server.runner.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * HTTP implementation of the {@link ResourceProvider}.
 * <p>
 * Utilizes the native Java {@link HttpClient} to perform file downloads.
 * Features include:
 * <ul>
 * <li>Automatic follow-redirects ({@code HttpClient.Redirect.ALWAYS}).</li>
 * <li>Direct file streaming via {@link HttpResponse.BodyHandlers#ofFile(Path)}.</li>
 * <li>Atomic failure handling: deletes the destination file if the download fails or returns a non-success status code.</li>
 * </ul>
 */
public abstract non-sealed class HttpResourceProvider extends ResourceProvider {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    /**
     * Executes an HTTP GET request and streams the response directly to a file.
     *
     * @param request the URI and destination file details
     */
    @Override
    protected final void provide(final @NonNull ResourceRequest request) throws ResourceDownloadException {
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(request.uri())
                .GET()
                .timeout(request.timeout())
                .build();

        Try.run(() -> sendHttpRequest(request, httpRequest))
                .onFailure($ -> FileUtil.deleteFile(request.destinationFile()))
                .onFailure(InterruptedException.class, $ -> Thread.currentThread().interrupt())
                .getOrElseThrow(ResourceDownloadException::new);
    }

    private void sendHttpRequest(
            final @NonNull ResourceRequest request,
            final @NonNull HttpRequest httpRequest
    ) throws ResourceDownloadException, IOException, InterruptedException {
        final File destinationFile = request.destinationFile();
        final Path destinationPath = destinationFile.toPath();

        final HttpResponse.BodyHandler<Path> responseBodyHandler = HttpResponse.BodyHandlers.ofFile(destinationPath);
        final HttpResponse<Path> httpResponse = this.httpClient.send(httpRequest, responseBodyHandler);

        final int statusCode = httpResponse.statusCode();
        final boolean isStatusCodeAllow = statusCode >= 200 && statusCode < 300;

        if (!isStatusCodeAllow || Files.size(destinationPath) <= 0) {
            throw new ResourceDownloadException(
                    "Failed to download resource %s. Server returned HTTP %s".formatted(destinationFile.getName(), statusCode)
            );
        }
    }
}
