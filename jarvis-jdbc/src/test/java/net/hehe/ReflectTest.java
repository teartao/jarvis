package net.hehe;

import com.alibaba.fastjson.JSONObject;
import net.hehe.example.User;
import net.hehe.utils.MySQLBuilder;
import net.hehe.utils.RefelectUtils;
import net.hehe.utils.SQLHelper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author neo·tao
 * @Date 2017/11/23
 * @Desc
 */

public class ReflectTest {
    public static JdbcTemplate jdbcTemplate;
    public static void main(String[] args) throws IllegalAccessException {
        User user = new User();
        user.setPassword("111");
        user.setSex("man");
        user.setUsername("ZhangSan");

        System.out.println(RefelectUtils.getFieldsName(user));
        System.out.println(RefelectUtils.getFieldsValue(user));
        System.out.println(RefelectUtils.getNotNullFieldsName(user));
        System.out.println(RefelectUtils.getNotNullFieldsValue(user));


        SQLHelper sql=new MySQLBuilder<User>() {
            @Override
            public String getTableName() {
                return "user";
            }

            @Override
            public JSONObject wrap(Object obj) {
                return null;
            }

            @Override
            public Object unwrap(JSONObject jsonObject) {
                return null;
            }
        };

        System.out.println(sql.getInsertSQL(user));
        System.out.println(sql.getDeleteSQL(user));
        System.out.println(sql.getUpdateSQL(user));
        System.out.println(sql.getSelectSQL(user));
    }
}
