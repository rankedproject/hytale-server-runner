package net.rankedproject.hytale.boot;

import net.rankedproject.hytale.boot.mod.ModExtension;
import org.gradle.api.Action;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.net.URI;

@SuppressWarnings("unused")
public abstract class HytaleBootExtension implements Serializable {

    @Nested
    public abstract @NotNull ModExtension getModExtension();

    public void mods(final @NotNull Action<? super ModExtension> action) {
        action.execute(getModExtension());
    }

    public abstract @NotNull DirectoryProperty getServerDirectory();

    public abstract @NotNull DirectoryProperty getRunDirectory();

    public abstract @NotNull DirectoryProperty getModDirectory();

    public abstract @NotNull Property<File> getAssets();

    public abstract @NotNull Property<URI> getServerDownloadUri();

    public abstract @NotNull Property<File> getServerJar();

    public abstract @NotNull Property<String> getServerJarMainClass();

    public abstract @NotNull ListProperty<String> getJvmArgs();

    @Inject
    public HytaleBootExtension(final @NotNull ObjectFactory objectFactory, final @NotNull ProjectLayout layout) {
        getRunDirectory().convention(layout.getProjectDirectory().dir("run"));
        getServerDirectory().convention(getRunDirectory().dir("Server"));
        getModDirectory().convention(getServerDirectory().dir("mods"));
        getServerJar().convention(getServerDirectory().file("HytaleServer.jar").map(RegularFile::getAsFile));
        getAssets().convention(getRunDirectory().file("Assets.zip").map(RegularFile::getAsFile));
        getServerDownloadUri().convention(URI.create("https://downloader.hytale.com/hytale-downloader.zip"));
        getServerJarMainClass().convention("com.hypixel.hytale.Main");
    }
}
