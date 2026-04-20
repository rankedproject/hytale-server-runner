package wtf.ranked.hytale.server.runner.extension;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OnlineMode {

    AUTHENTICATED("authenticated"),
    OFFLINE("offline");

    private final String onlineMode;
}
