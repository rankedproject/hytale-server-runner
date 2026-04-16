package net.rankedproject.hytale.boot.task.type;

import net.rankedproject.hytale.boot.HytaleBootPlugin;
import net.rankedproject.hytale.boot.step.Step;
import org.gradle.api.DefaultTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GlobalRunningTask extends DefaultTask {

    protected GlobalRunningTask() {
        setGroup(HytaleBootPlugin.PLUGIN_GROUP);
    }

    public abstract @NotNull List<Class<? extends Step>> steps();
}