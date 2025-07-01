package com.yongcoding.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    // 수정할 수 있는 필드들에 정의
    private String title;
    private String content;

    // @Builder는 빌드할 때 값 집어넣을떄 중간마다 호출되는 것이 아닌.
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditor.PostEditorBuilder builder() {
        return new PostEditor.PostEditorBuilder();
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {}

        public PostEditor.PostEditorBuilder title (final String title) {
            if (title != null) {
                this.title = title;
            }
            return this;
        }

        public PostEditor.PostEditorBuilder content (final String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
