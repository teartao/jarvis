package net.hehe.jdbc;

import com.alibaba.fastjson.JSONObject;
import net.hehe.utils.SQLHelper;
import net.hehe.utils.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author TaoLei
 * @Date 16/6/21
 * @Since
 * @Desc
 */
@Repository
public abstract class BaseDAO<T> implements GenericCRUD {

    private static Logger logger = LoggerFactory.getLogger(BaseDAO.class);

    @Autowired
    private SQLHelper mySqlBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public T findOne(String id, Class clazz) {
        return null;
    }

    @Override
    public T find(Map param, Class clazz) {
        return null;
    }

    @Override
    public T find(String sql, Map param) {
        return null;
    }

    @Override
    public Object find(String sql, Map param, Class clazz) {
        return null;
    }

    @Override
    public List<T> findAll(Map param) {
        return null;
    }

    @Override
    public int update(Object entity) {
        return 0;
    }

    @Override
    public int update(Object entity, Map param) {
        return 0;
    }

    @Override
    public int insert(Object entity) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public int delete(Object entity) {
        return 0;
    }
}
