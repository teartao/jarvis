package net.hehe.validate;

/**
 * @Author neo·tao
 * @Date 2017/12/20
 * @Desc
 */
public interface Validate<T> {

    boolean validate(String data,T param);
}
