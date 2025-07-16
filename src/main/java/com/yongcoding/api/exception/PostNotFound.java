package com.yongcoding.api.exception;

/**
 * 정책상 status: 404
 */
// 가능하면, unchecked exception을 사용하는 것이 좋다.
public class PostNotFound extends YologException {
    private static final String MESSAGE = "존재하지 않는 글입니다.";

    // 생성자쪽으로 기본메시지 값을 던져줌
    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
