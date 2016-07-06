package com.lv.test.helper;

/**
 * User: 吕勇
 * Date: 2016-07-06
 * Time: 10:36
 * Description:
 */
public class RestResult<T> {
    public static final int SUCCESS =100 ;
    public static final int SIGN_OUT =300 ;

    private int code=200;
    private String message="xxxx";
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
