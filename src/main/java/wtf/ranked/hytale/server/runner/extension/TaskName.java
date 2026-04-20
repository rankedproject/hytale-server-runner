package wtf.ranked.hytale.server.runner.extension;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskName {

    JAR("jar"),
    SHADOW_JAR("shadowJar"),
    ;

    private final String identifier;
}
