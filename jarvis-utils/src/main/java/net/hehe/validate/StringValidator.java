package net.hehe.validate;

/**
 * @Author neo·tao
 * @Date 2017/12/20
 * @Desc
 */
public class StringValidator implements Validate {
    @Override
    public boolean validate(String data, Object param) {
        return data != null && data.equals(param);
    }
}
