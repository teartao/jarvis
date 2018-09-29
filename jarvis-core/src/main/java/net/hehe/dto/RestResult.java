package net.hehe.dto;

import java.io.Serializable;

/**
 * @Author neotao
 * @Date 2018/9/29
 * @Version V0.0.1
 * @Desc
 */
public class RestResult<T> implements Serializable {
    private String code = "0000";
    private String msg = "success";
    private T data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return "0000".equals(this.code);
    }

    public void setResult(String code, String message) {
        this.code = code;
        this.msg = message;
    }
}
