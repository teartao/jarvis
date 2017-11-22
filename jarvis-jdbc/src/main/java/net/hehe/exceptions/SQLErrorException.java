package net.hehe.exceptions;

/**
 * @Author neo·tao
 * @Date 2017/11/22
 * @Desc
 */
public class SQLErrorException extends RuntimeException {
    public SQLErrorException() {
        super();
    }

    public SQLErrorException(String message) {
        super(message);
    }
}
