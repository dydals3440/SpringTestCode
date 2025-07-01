package com.yongcoding.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    // 수정할 수 있는 필드들에 정의
    private String title;
    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
