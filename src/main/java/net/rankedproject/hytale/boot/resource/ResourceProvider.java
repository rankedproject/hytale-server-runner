package net.rankedproject.hytale.boot.resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public abstract sealed class ResourceProvider
        implements BuildService<BuildServiceParameters.None>
        permits HttpResourceProvider {

    public @NotNull ResourceProvider.ResourceRequest.Builder builder() {
        return ResourceRequest.builder(this);
    }

    protected abstract @NotNull CompletableFuture<Void> provide(@NotNull ResourceProvider.ResourceRequest request);

    protected record ResourceRequest(
            @NotNull URI uri,
            @NotNull File destinationFile,
            @NotNull Duration timeout
    ) {

        private static @NotNull ResourceProvider.ResourceRequest.Builder builder(final @NotNull ResourceProvider provider) {
            return new ResourceRequest.Builder(provider);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Builder {

            private final ResourceProvider provider;

            private URI uri;
            private File destinationFile;
            private Duration timeout = Duration.ofSeconds(5);

            public @NotNull Builder uri(final @NotNull String url) {
                this.uri = URI.create(url);
                return this;
            }

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

            private @NotNull ResourceProvider.ResourceRequest build() {
                return new ResourceRequest(this.uri, this.destinationFile, this.timeout);
            }
        }
    }
}
