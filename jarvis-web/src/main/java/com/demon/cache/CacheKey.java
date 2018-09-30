package com.demon.cache;

public class CacheKey {

   private String cacheKey;

   private String cacheId;

    public CacheKey(String cacheKey, String cacheId) {
        this.cacheKey = cacheKey;
        this.cacheId = cacheId;
    }

    public String cacheKey() {
        return cacheKey;
    }

    public String cacheId() {
        return cacheId;
    }
}
