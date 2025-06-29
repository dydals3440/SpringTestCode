package com.yongcoding.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
