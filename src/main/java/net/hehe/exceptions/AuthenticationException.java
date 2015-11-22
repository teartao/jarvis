package net.hehe.exceptions;

/**
 * author: taolei
 * date: 15/11/22.
 * description:认证失败异常
 */
public class AuthenticationException extends RuntimeException {
    private final String HTTP_STATUS = "401";

    public AuthenticationException() {
        super("Authentication failed");
    }

    public String getErrorCode() {
        return this.HTTP_STATUS;
    }
}
