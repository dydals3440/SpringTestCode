package com.yongcoding.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
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
// 응답 필요 없는 경우에는 JSON에 포함하지 않음 (굳이 필요한가 싶기도..?)
// 없는것도 대화 내용아닌가.
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    //  HashMap은 좋은 구조가 아님. 추후 리팩토링. (Map -> DTO)
    private Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
