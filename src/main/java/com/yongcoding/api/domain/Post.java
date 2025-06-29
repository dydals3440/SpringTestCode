package com.yongcoding.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
