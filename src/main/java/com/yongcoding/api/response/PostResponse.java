package com.yongcoding.api.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 클래스를 만듬.
 */
@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;

    // 생성자에 @Builder를 붙이면 빌더 패턴으로 객체를 생성할 수 있음.
    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
