package wtf.ranked.hytale.server.runner.resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.gradle.api.services.BuildService;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.extension.HytaleExtensionParameters;
import wtf.ranked.hytale.server.runner.resource.ResourceProvider.ResourceRequest.Builder;

import java.io.File;
import java.net.URI;
import java.time.Duration;

/**
 * Base build service for acquiring external resources.
 * <p>
 * Uses a sealed hierarchy to strictly control how resources are fetched.
 * Provides a fluent {@link ResourceRequest.Builder} to construct resource requests.
 */
public abstract sealed class ResourceProvider
        implements BuildService<HytaleExtensionParameters>
        permits HttpResourceProvider {

    /**
     * Creates a new builder for a resource acquisition request.
     *
     * @return a fluent builder instance
     */
    public final @NonNull Builder builder() {
        return ResourceRequest.builder(this);
    }

    /**
     * Core logic for fulfilling a resource request.
     *
     * @param request the details of the resource to fetch
     */
    protected abstract void provide(@NonNull ResourceRequest request);

    protected record ResourceRequest(
            @NonNull URI uri,
            @NonNull File destinationFile,
            @NonNull Duration timeout
    ) {

        private static @NonNull Builder builder(final @NonNull ResourceProvider provider) {
            return new ResourceRequest.Builder(provider);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder {

            private final ResourceProvider provider;

            private URI uri;
            private File destinationFile;
            private Duration timeout;

            public @NonNull Builder uri(final @NonNull URI uri) {
                this.uri = uri;
                return this;
            }

            public @NonNull Builder destinationFile(final @NonNull File destinationFile) {
                this.destinationFile = destinationFile;
                return this;
            }

            public @NonNull Builder timeout(final @NonNull Duration timeout) {
                this.timeout = timeout;
                return this;
            }

            public void provide() {
                this.provider.provide(build());
            }

            private @NonNull ResourceRequest build() {
                return new ResourceRequest(this.uri, this.destinationFile, this.timeout);
            }
        }
    }
}
