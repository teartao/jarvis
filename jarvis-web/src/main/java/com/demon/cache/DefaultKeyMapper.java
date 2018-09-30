package com.demon.cache;

public class DefaultKeyMapper implements KeyMapper {

    @Override
    public String apply(Object value) {
        return value == null
                ? "null"
                : value instanceof Class
                ? ((Class<?>) value).getName()
                : String.valueOf(value);
    }
}
