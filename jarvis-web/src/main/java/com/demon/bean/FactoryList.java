package com.demon.bean;

import java.util.List;

public interface FactoryList<K, E extends MatchingBean<K>> extends List<E> {

    E getBean(K factor);
    
}