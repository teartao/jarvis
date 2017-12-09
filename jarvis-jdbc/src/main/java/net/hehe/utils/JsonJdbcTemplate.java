package net.hehe.utils;

import net.hehe.example.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by taolei on 2017/12/9.
 */
@Component
public class JsonJdbcTemplate {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public Object queryForObject(Object obj) {
        return new Object();
    }

    public Integer insert(String sql) {
        return jdbcTemplate.update(sql);
    }

    public void update(final User user) {

    }

    public void delete(User user) {

    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }


}
