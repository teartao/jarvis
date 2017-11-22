package net.hehe.utils;

import net.hehe.exceptions.SQLErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author neo·tao
 * @Date 2017/11/22
 * @Desc
 */
public class SQLUtils {

    public static final String AND_TAG="AND";
    public static final String COMMA_TAG=",";
    private static Logger logger = LoggerFactory.getLogger(SQLUtils.class);

    private static String cutDataFromList(List<String> list) {
        return String.valueOf(list).replace("[", "").replace("]", "");
    }

    public static String getColumnsName(Object entity) throws IllegalAccessException {
        return cutDataFromList(RefelectUtils.getNotNullFieldsName(entity));
    }

    public static String getColumnsValue(Object entity) throws IllegalAccessException {
        return cutDataFromList(RefelectUtils.getNotNullFieldsValue(entity));
    }

    /**
     * get key=value  String of columns and split with splitTag
     *
     * @param entity
     * @param splitTag
     * @return key=value
     * @throws IllegalAccessException
     */
    public static String getKeyValue(Object entity, String splitTag) throws IllegalAccessException {
        String columnsName = getColumnsName(entity);
        String columnsValue = getColumnsValue(entity);

        String[] columnsNameArr = columnsName.split(",");
        String[] columnsValueArr = columnsValue.split(",");

        StringBuilder keyValueStr = new StringBuilder();
        if (columnsNameArr.length == columnsValueArr.length) {
            for (int i = 0; i < columnsNameArr.length; i++) {
                if (keyValueStr.length() > 0) {
                    keyValueStr.append(" ").append(splitTag).append(" ");
                }
                keyValueStr.append(columnsNameArr[i]).append("=").append("'").append(columnsValueArr[i]).append("'");
            }

            return keyValueStr.toString();
        } else {
            throw new SQLErrorException("column count doesn't match value count");
        }
    }
}
