package com.yongcoding.api.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// abstract class로 선언하여, 다른 예외 클래스들이 상속받을 수 있도록 한다.
@Getter
public abstract class YologException extends RuntimeException {


    public final Map<String, String> validation = new HashMap<>();


    public YologException(String message) {
        super(message);
    }

    public YologException(String message, Throwable cause) {
        super(message, cause);
    }

    // 반드시 statusCode 메소드를 구현하도록 강제한다.
    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
