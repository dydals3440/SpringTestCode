package com.yongcoding.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        // 테스트마다 한번씩 실행됨.
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 아무것도 반환하지 않는다.")
    void test() throws Exception {
        // given
        // PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        // ObjectMapper를 사용하여 객체를 JSON 문자열로 변환 (Getter가 있으니)
        // ObjectMapper objectMapper = new ObjectMapper();
        // SpringBoot는 이미 ObjectMapper가 빈으로 등록되어 있음. 필요시에는 빈을 변환하자
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                        .title(null)
                        .content("글 내용입니다.")
                        .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validations.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장됨")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회 (제목은 10글자 이하로 잘라서 응답)")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("12345678910")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        // 클라이언트 요구사항
        // json 응답에서 타이틀 값 길이를 최대 10글자로 해주세요.
        // -> 보통은 클라에서 처리, Post 엔티티에서 getter를 오버라이드해서 처리할 수 있다.
        // Getter 오버라이드시 다른 정책이 추가되면, 복잡해짐.


        // expected (when + then)
        mockMvc.perform(get("/posts/{id}", post.getId())
                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567891"))
                .andExpect(jsonPath("$.content").value("내용입니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("YOLOG 제목 " + i)
                        .content("YOLOG 내용 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].id").value(30L))
                .andExpect(jsonPath("$[0].title").value("YOLOG 제목 30"))
                .andExpect(jsonPath("$[0].content").value("YOLOG 내용 30"))
                .andDo(print());
    }
}