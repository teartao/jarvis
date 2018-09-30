package com.demon.cache;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ListKeyMapper implements KeyMapper {

    @Override
    public String apply(Object value) {
        if (value == null) {
            return "null";
        }
        List<?> list = (List<?>) value;
        if (CollectionUtils.isEmpty(list)) {
            return "_";
        }
        return list.stream().map(String::valueOf).collect(Collectors.joining("_"));
    }
}
