package com.demon.cache;

import java.util.ArrayList;
import java.util.List;

public class MultipleArgumentMatcher<A, V> implements ArgumentMatcher<A> {

    private A argument;

    private List<V> values;

    public MultipleArgumentMatcher(A argument, List<V> values) {
        this.argument = argument;
        this.values = values;
    }

    @Override
    public A get() {
        return argument;
    }

    public List<V> values() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }
}
