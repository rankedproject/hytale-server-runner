package net.rankedproject.hytale.boot.mod.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.mod.Mod;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ModDownloaderStrategy<M extends Mod>
        implements BuildService<ModDownloaderStrategy.ModDownloaderStrategyParameters> {

    public void downloadMod(final @NotNull M mod) {
        final File modsDirectory = getParameters().getModDirectory().get().getAsFile();
        if (!modsDirectory.exists()) {
            modsDirectory.mkdirs();
        }

        if (isModInstalled(mod.getFileName())) {
            return;
        }

        download(mod);
    }

    protected boolean isModInstalled(final @NotNull String identifier) {
        final File modsDirectory = getParameters().getModDirectory().get().getAsFile();
        return new File(modsDirectory, identifier).exists();
    }

    protected abstract void download(@NotNull M mod);

    public interface ModDownloaderStrategyParameters extends BuildServiceParameters {
        @NotNull DirectoryProperty getModDirectory();
    }
}
