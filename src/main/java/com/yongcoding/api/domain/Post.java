package com.yongcoding.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // 자바 String, DB 에서는 LongText 형태로 넘어감.
    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    서비스의 정책을 절대 안넣는게 좋음.
//    public String getTitle() {
//        return this.title.substring(0, 10);
//    }

//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    // Fixed: PostEditor를 사용하여 수정하는 메서드
    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
