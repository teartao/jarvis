package net.hehe.utils;

import net.hehe.exceptions.SQLErrorException;
import org.springframework.stereotype.Component;

/**
 * @Author TaoLei
 * @Date 06/20/2016
 * @Since
 * @Desc
 */
@Component
public abstract class MySQLBuilder<E> implements SQLHelper {

    @Override
    public String getInsertSQL(Object entity) {
        StringBuilder sql = new StringBuilder();
        try {
            String nameStr = SQLUtils.getColumnsName(entity);
            String valueStr = SQLUtils.getColumnsValue(entity);
            sql.append("INSERT INTO ").append(getTableName()).append("(").append(nameStr).append(") VALUES(").append(valueStr).append(")");
        } catch (IllegalAccessException e) {
            throw new SQLErrorException("can not access " + entity.getClass() + " ,because of method is private !");
        }
        return sql.toString();
    }


    @Override
    public String getDeleteSQL(Object entity) {
        StringBuilder sql = new StringBuilder();
        try {
            String paramStr = SQLUtils.getKeyValue(entity, SQLUtils.AND_TAG);
            sql.append("DELETE FROM ").append(getTableName()).append(" WHERE ").append(paramStr);
        } catch (IllegalAccessException e) {
            throw new SQLErrorException("can not access " + entity.getClass() + " ,because of method is private !");
        }
        return sql.toString();
    }

    @Override
    public String getUpdateSQL(Object entity) {
        StringBuilder sql = new StringBuilder();
        try {
            String paramStr = SQLUtils.getKeyValue(entity, SQLUtils.COMMA_TAG);
            sql.append("UPDATE ").append(getTableName()).append(" SET ").append(paramStr);
        } catch (IllegalAccessException e) {
            throw new SQLErrorException("can not access " + entity.getClass() + " ,because of method is private !");
        }
        return sql.toString();
    }

    @Override
    public String getSelectSQL(Object entity) {
        StringBuilder sql = new StringBuilder();
        try {
            String paramStr = SQLUtils.getKeyValue(entity, SQLUtils.COMMA_TAG);
            sql.append("SELECT * FROM ").append(getTableName()).append(" WHERE ").append(paramStr);
        } catch (IllegalAccessException e) {
            throw new SQLErrorException("can not access " + entity.getClass() + " ,because of method is private !");
        }

        return sql.toString();
    }
}
