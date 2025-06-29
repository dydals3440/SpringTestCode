package com.yongcoding.api.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "code":"400",
 * "message" : "잘못된 요청입니다.",
 * "validation" {
 * "title": "타이틀을 입력해주세요",
 * "content": "콘텐츠를 입력해주세요"
 * }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
//  HashMap은 좋은 구조가 아님. 추후 리팩토링. (Map -> DTO)
    private Map<String, String> validations = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validations.put(fieldName, errorMessage);
    }
}
