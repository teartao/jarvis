package net.hehe.jdbc;

import java.util.List;
import java.util.Map;

/**
 * Created by TaoLei on 2016/6/21.
 */
public interface GenericCRUD<T> {
    T findOne(String id, Class<T> clazz);

    T find(Map param, Class<T> clazz);

    T find(String sql, Map<String, String> param);

    T find(String sql, Map param, Class<T> clazz);

    List<T> findAll(Map<String, String> param);

    int update(T entity);

    int update(T entity, Map<String, String> param);

    int insert(T entity);

    int delete(String id);

    int delete(Object entity);
}
