package com.example.usercentor.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(Code code) {
        this(code.getCode(),null,code.getMessage(), code.getDescription());
    }
    public BaseResponse(Code code,T data) {
        this(code.getCode(),data,code.getMessage(), code.getDescription());
    }

    public BaseResponse(int code, String message, String description) {
        this(code,null,message,description);
    }
}
