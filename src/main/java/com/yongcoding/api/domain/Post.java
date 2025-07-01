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

    // post.edit(1, 2, 3, 4) 이런식으로 너무 늘어나면 처리하기 애매해짐
    // Post 도메인내에서 수정할 수 있는 부분을 따로 분리해서 제한 포인트를 두기 위해서 PostEditor를 사용.
    // 또 하나는, 클라이언트의 약속에 따라서 달라질 수 잇느 ㄴ상황.
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


//    public void edit(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }


}
