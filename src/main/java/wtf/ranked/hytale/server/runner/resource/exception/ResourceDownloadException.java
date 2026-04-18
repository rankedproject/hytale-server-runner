package wtf.ranked.hytale.server.runner.resource.exception;

import org.jspecify.annotations.NonNull;

public final class ResourceDownloadException extends RuntimeException {

    public ResourceDownloadException(final @NonNull String message) {
        super(message);
    }

    public ResourceDownloadException(final @NonNull Throwable throwable) {
        super(throwable);
    }
}
