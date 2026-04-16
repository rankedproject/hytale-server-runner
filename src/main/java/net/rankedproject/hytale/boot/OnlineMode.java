package net.rankedproject.hytale.boot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OnlineMode {

    AUTHENTICATED("authenticated"),
    OFFLINE("offline");

    private final String onlineMode;
}
