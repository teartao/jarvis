package com.demon.cache;

import java.util.concurrent.TimeUnit;

public class Condition {

    private long expire;

    private TimeUnit expireUnit;

    private int maximumSize;

    public static Condition of(Cached cached) {
        Condition condition = new Condition();
        condition.expire = cached.expire();
        condition.expireUnit = cached.expireUnit();
        condition.maximumSize = cached.maximumSize();
        return condition;
    }

    public long expire() {
        return expire;
    }

    public TimeUnit expireUnit() {
        return expireUnit;
    }

    public int maximumSize() {
        return maximumSize;
    }
}
