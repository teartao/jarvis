package net.hehe.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author TaoLei
 * @Date 06/20/2016
 * @Since
 * @Desc
 */

public interface SQLHelper {
    String getTableName();

    JSONObject wrap(Object obj);

    Object unwrap(JSONObject jsonObject);

    String getInsertSQL(Object entity);

    String getDeleteSQL(Object entity);

    String getUpdateSQL(Object entity);

    String getSelectSQL(Object entity);

}
