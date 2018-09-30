package com.demon.cache;

import java.util.stream.Stream;

public enum Media {

    REDIS, GUAVA, MAP;

    public static Media of(String media) {
        return Stream
                .of(values()).filter(m -> m.name().equalsIgnoreCase(media))
                .findFirst()
                .orElse(REDIS);
    }
}
