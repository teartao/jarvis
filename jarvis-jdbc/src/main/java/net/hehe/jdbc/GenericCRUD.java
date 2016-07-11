package net.hehe.jdbc;

import java.util.List;
import java.util.Map;

/**
 * Created by TaoLei on 2016/6/21.
 */
public interface GenericCRUD<E> {
    <T> T findOne(String id, Class<T> clazz);

    <T> T find(Map param, Class<T> clazz);

    <T> T find(String sql, Map<String, String> param);

    <T> T find(String sql, Map<String, String> param, Class<T> clazz);

    <T> List<T> findAll(Map<String, String> param);

    String update(E entity);

    String update(E entity, Map<String, String> param);

    String insert(E entity);

    void delete(String id);

    void delete(E entity);
}
