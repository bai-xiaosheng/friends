package com.example.usercentor.exception;

import com.example.usercentor.common.Code;

public class BusinessException extends RuntimeException{

    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(Code code) {
        super(code.getMessage());
        this.code = code.getCode();
        this.description = code.getDescription();
    }
    public BusinessException(Code code, String description) {
        super(code.getMessage());
        this.code = code.getCode();
        this.description = description;
    }
    public BusinessException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
