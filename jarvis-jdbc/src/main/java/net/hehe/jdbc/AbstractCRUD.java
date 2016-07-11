package net.hehe.jdbc;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by hasee on 2016/6/20.
 */
public abstract class AbstractCRUD<T> {
    public abstract String getTableName();

    public abstract JSONObject wrap(T obj);

    public abstract <T> String unwrap(JSONObject jsonObject);
}
