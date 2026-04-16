package net.rankedproject.hytale.boot.resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.extension.HytaleExtensionParameters;
import net.rankedproject.hytale.boot.resource.ResourceProvider.ResourceRequest.Builder;
import org.gradle.api.services.BuildService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
    public final @NotNull Builder builder() {
        return ResourceRequest.builder(this);
    }

    /**
     * Core logic for fulfilling a resource request.
     *
     * @param request the details of the resource to fetch
     * @return a future that completes when the resource is stored locally
     */
    protected abstract @NotNull CompletableFuture<Void> provide(@NotNull ResourceRequest request);

    protected record ResourceRequest(
            @NotNull URI uri,
            @NotNull File destinationFile,
            @NotNull Duration timeout
    ) {

        private static @NotNull Builder builder(final @NotNull ResourceProvider provider) {
            return new ResourceRequest.Builder(provider);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder {

            private final ResourceProvider provider;

            private URI uri;
            private File destinationFile;
            private Duration timeout = Duration.ofSeconds(5);

            public @NotNull Builder uri(final @NotNull URI uri) {
                this.uri = uri;
                return this;
            }

            public @NotNull Builder destinationFile(final @NotNull File destinationFile) {
                this.destinationFile = destinationFile;
                return this;
            }

            public @NotNull Builder timeout(final @NotNull Duration timeout) {
                this.timeout = timeout;
                return this;
            }

            public @NotNull CompletableFuture<Void> provide() {
                return this.provider.provide(build());
            }

            private @NotNull ResourceRequest build() {
                return new ResourceRequest(this.uri, this.destinationFile, this.timeout);
            }
        }
    }
}
