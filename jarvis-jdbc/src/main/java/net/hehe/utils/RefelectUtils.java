package net.hehe.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author neo·tao
 * @Date 2017/11/22
 * @Desc
 */
public class RefelectUtils {
    private static Field[] getFields(Object object) {
        Class clazz = object.getClass();
        return clazz.getDeclaredFields();
    }

    private static List<String> getFieldsName(Object object, boolean includeNull) throws IllegalAccessException {
        List<String> nameList = new ArrayList<>();
        Field[] fields = getFields(object);
        for (Field field : fields) {
            field.setAccessible(true);
            if (includeNull) {
                nameList.add(field.getName());
            } else {
                if (field.get(object) != null) {
                    nameList.add(field.getName());
                }
            }
        }
        return nameList;
    }

    private static List<String> getFieldsValue(Object object, boolean includeNull) throws IllegalAccessException {
        List<String> valueList = new ArrayList<>();
        Field[] fields = getFields(object);
        for (Field field : fields) {
            field.setAccessible(true);
            if (includeNull) {
                valueList.add(String.valueOf(field.get(object)));
            } else {
                if (field.get(object) != null) {
                    valueList.add(String.valueOf(field.get(object)));
                }
            }
        }
        return valueList;
    }

    public static List<String> getMapKey(Map<String, Object> object, boolean includeNull) {
        List<String> nameList = new ArrayList<>();
        Set<String> keySet = object.keySet();
        for (String key : keySet) {
            if (includeNull) {
                nameList.add(key);
            } else {
                if (object.get(key) != null) {
                    nameList.add(key);
                }
            }
        }
        return nameList;
    }

    private static List<String> getMapValue(Map<String, Object> object, boolean includeNull) {
        List<String> valueList = new ArrayList<>();
        Set<String> keySet = object.keySet();
        for (String key : keySet) {
            if (includeNull) {
                valueList.add(String.valueOf(object.get(key)));
            } else {
                if (object.get(key) != null) {
                    valueList.add(String.valueOf(object.get(key)));
                }
            }
        }
        return valueList;
    }


    public static List<String> getFieldsName(Object object) throws IllegalAccessException {
        if (object instanceof Map) {
            return getMapKey((Map<String, Object>) object, false);
        } else {
            return getFieldsName(object, false);
        }
    }

    public static List<String> getFieldsValue(Object object) throws IllegalAccessException {
        if (object instanceof Map) {
            return getMapValue((Map<String, Object>) object, false);
        } else {
            return getFieldsValue(object, false);
        }
    }

    public static List<String> getNotNullFieldsName(Object object) throws IllegalAccessException {
        if (object instanceof Map) {
            return getMapKey((Map<String, Object>) object, true);
        } else {
            return getFieldsName(object, true);
        }
    }

    public static List<String> getNotNullFieldsValue(Object object) throws IllegalAccessException {
        if (object instanceof Map) {
            return getMapValue((Map<String, Object>) object, true);
        } else {
            return getFieldsValue(object, true);
        }
    }
}
