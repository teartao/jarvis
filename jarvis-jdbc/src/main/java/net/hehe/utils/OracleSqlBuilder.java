package net.hehe.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author neo·tao
 * @Date 2017/11/22
 * @Desc
 */
public abstract class OracleSqlBuilder implements SQLHelper {

    @Override
    public String getInsertSQL(Object entity) {
        return null;
    }

    @Override
    public String getDeleteSQL(Object entity) {
        return null;
    }

    @Override
    public String getUpdateSQL(Object entity) {
        return null;
    }

    @Override
    public String getSelectSQL(Object entity) {
        return null;
    }
}
