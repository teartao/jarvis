package com.demon.cache;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComparableListKeyMapper implements KeyMapper {

    @Override
    @SuppressWarnings("unchecked")
    public String apply(Object value) {
        if (value == null) {
            return "null";
        }
        List<Comparable> list = (List<Comparable>) value;
        Collections.sort(list);
        if (CollectionUtils.isEmpty(list)) {
            return "_";
        }
        return list.stream().map(String::valueOf).collect(Collectors.joining("_"));
    }
}
