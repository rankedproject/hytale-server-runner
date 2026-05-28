package wtf.ranked.hytale.server.runner.server;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class ServerLauncherBuilder {

    private final ServerLauncher launcher;
    private final Set<Module> modules = new HashSet<>();

    public ServerLauncherBuilder module(final Module module) {
        modules.add(module);
        return this;
    }

    public void launch() {
        launcher.launch(modules);
    }
}
