package com.yongcoding.api.exceptions;

import lombok.Getter;

/**
 * 정책상 status: 400
 */
@Getter
public class InvalidRequest extends YologException {
    public static final String MESSAGE = "잘못된 요청입니다.";


    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        // 생성하는 시점에 상위에있는 YologException에 필드네임과 메시지를 추가
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
