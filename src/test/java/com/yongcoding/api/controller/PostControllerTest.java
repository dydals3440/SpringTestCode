package com.yongcoding.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.request.PostEdit;
import com.yongcoding.api.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @DisplayName("글 작성 요청시 title 값은 필수")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
            .title(null)
            .content("글 내용입니다.")
            .build();
        String json = objectMapper.writeValueAsString(request);

        // expect: 400 + 상태 메시지 확인 + 바디가 빈 문자열
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isBadRequest())
            .andExpect(status().reason("Invalid request content."))
            .andExpect(content().string(""))   // 빈 바디
            .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시 DB에 값이 저장됨")
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

        Post post = postRepository.findAll()
            .get(0);
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
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가리킨다.")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
            .mapToObj(i -> Post.builder()
                .title("foo" + i)
                .content("bar" + i)
                .build())
            .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(10)))
            .andExpect(jsonPath("$[0].title").value("foo19"))
            .andExpect(jsonPath("$[0].content").value("bar19"))
            .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
            .title("매튜")
            .content("서울강동")
            .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("매튜튜")
            .content("서울강동")
            .build();

        // when
        mockMvc.perform(patch("/posts/{id}", post.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit))
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        Post post = Post.builder()
            .title("매튜")
            .content("서울강동")
            .build();
        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
            .title("매튜튜")
            .content("서울강동")
            .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'가 포함되면 예외 발생")
    void test11() throws Exception {
        // Given
        PostCreate request = PostCreate.builder()
            .title("바보입니다.")
            .content("내용입니다.")
            .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}
