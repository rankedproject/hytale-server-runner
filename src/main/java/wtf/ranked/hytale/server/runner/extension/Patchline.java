package wtf.ranked.hytale.server.runner.extension;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Patchline {

    RELEASE("release"),
    PRE_RELEASE("pre-release");

    private final String identifier;
}
