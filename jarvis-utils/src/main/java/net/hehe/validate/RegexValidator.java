package net.hehe.validate;

import java.util.regex.Pattern;

/**
 * @Author neo·tao
 * @Date 2017/12/20
 * @Desc
 */
public class RegexValidator<T> implements Validate {
    @Override
    public boolean validate(String data, Object regex) {
        return Pattern.matches(String.valueOf(regex), data);
    }
}
