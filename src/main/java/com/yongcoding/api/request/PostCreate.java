package com.yongcoding.api.request;

import com.yongcoding.api.exceptions.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {
    @NotBlank(message = "타이틀을 입력해주세요")
    private final String title;


    @NotBlank(message = "콘텐츠를 입력해주세요")
    private final String content;


    //    public PostCreate(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // builder 패턴 장점
    // 1. 가독성 좋음.
    // 2. 값 생성에 대한 유연함.
    // 3. 필요한 값만 받을 수 있다. // -> 오버로딩 가능한 조건
    // 4. 객체의 불변성

    // final 필드의 경우, 생성자에서만 값을 설정할 수 있습니다.
    public PostCreate changeTitle(String title) {
        return PostCreate.builder()
                .title(title)
                .content(content)
                .build();
    }

    // starter-validation을 사용하여, 유효성 검사를 할 수 있습니다.
    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
