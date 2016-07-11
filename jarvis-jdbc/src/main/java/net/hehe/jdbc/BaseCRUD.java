package net.hehe.jdbc;

import java.util.List;
import java.util.Map;

/**
 * @Author TaoLei
 * @Date 16/6/21
 * @Since
 * @Desc
 */
public class BaseCRUD implements GenericCRUD {

    @Override
    public Object findOne(String id, Class clazz) {

        return null;
    }

    @Override
    public Object find(Map param, Class clazz) {
        return null;
    }

    @Override
    public Object find(String sql, Map param) {
        return null;
    }

    @Override
    public Object find(String sql, Map param, Class clazz) {
        return null;
    }

    @Override
    public List findAll(Map param) {
        return null;
    }

    @Override
    public String update(Object entity) {
        return null;
    }

    @Override
    public String insert(Object entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public String update(Object entity, Map param) {
        return null;
    }
}
