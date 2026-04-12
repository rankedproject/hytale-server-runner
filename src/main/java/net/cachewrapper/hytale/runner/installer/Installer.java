package net.cachewrapper.hytale.runner.installer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public abstract sealed class Installer permits HttpInstaller {

    public @NotNull Installer.InstallerData.Builder builder() {
        return InstallerData.builder(this);
    }

    protected abstract CompletableFuture<Void> install(@NotNull InstallerData installerData);

    protected record InstallerData(
            @NotNull URI uri,
            @NotNull Path destinationPath,
            @NotNull Duration timeout
    ) {

        private static @NotNull InstallerData.Builder builder(final @NotNull Installer installer) {
            return new InstallerData.Builder(installer);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Builder {

            private final Installer installer;

            private URI uri;
            private Path destinationPath;
            private Duration timeout = Duration.ofSeconds(5);

            public @NotNull Builder uri(final @NotNull String url) {
                this.uri = URI.create(url);
                return this;
            }

            public @NotNull Builder uri(final @NotNull URI uri) {
                this.uri = uri;
                return this;
            }

            public @NotNull Builder destinationPath(final @NotNull Path destinationPath) {
                this.destinationPath = destinationPath;
                return this;
            }

            public @NotNull Builder destinationPath(final @NotNull String destinationPath) {
                this.destinationPath = Path.of(destinationPath);
                return this;
            }

            public @NotNull Builder timeout(final @NotNull Duration timeout) {
                this.timeout = timeout;
                return this;
            }

            public @NotNull CompletableFuture<Void> install() {
                return this.installer.install(build());
            }

            private @NotNull InstallerData build() {
                return new InstallerData(uri, destinationPath, timeout);
            }
        }
    }
}
