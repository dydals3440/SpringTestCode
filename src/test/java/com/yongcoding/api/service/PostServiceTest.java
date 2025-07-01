package com.yongcoding.api.service;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.request.PostSearch;
import com.yongcoding.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void Test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void Test2() {
        // given -> RequestPost라는 Entity를 생성하고 Repository에 저장한다.
        Post requestPost = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(requestPost);


        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("제목입니다.", response.getTitle());
        assertEquals("내용입니다.", response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given (lambda 표현식으로 작성)
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                         .title("foo" + i)
                         .content("bar" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }
}