package com.yongcoding.api.exceptions;

// 가능하면, unchecked exception을 사용하는 것이 좋다.
public class PostNotFound extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }
}
