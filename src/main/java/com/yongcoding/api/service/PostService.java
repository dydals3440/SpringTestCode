package com.yongcoding.api.service;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    // Field Injection (Autowired)는 테스트 케이스 이외의 상황에서는 비선호.
    // 생성자 인젝션을 활용해서 PostRepository를 주입받는다.
    private final PostRepository postRepository;

    // Case 1. 저장한 데이터 Entity -> response로 반환
//    public Post write(PostCreate postCreate) {
//        Post post = Post.builder()
//                .title(postCreate.getTitle())
//                .content(postCreate.getContent())
//                .build();
//
//       return postRepository.save(post);
//    }

    // Case 2. 저장한 데이터 primary_id -> response로 반환
    // -> 클라이언트에서 수신한 id를 글 조회 API에 전달하여 글 데이터를 조회한다.
//    public Long write(PostCreate postCreate) {
//        Post post = Post.builder()
//                .title(postCreate.getTitle())
//                .content(postCreate.getContent())
//                 .build();
//
//        postRepository.save(post);
//
//       return post.getId();
//    }
    // Case 3. 응답 필요 없음 -> 클라이언트에서 모든 Post(글) 데이터 context를 잘 관리함.

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

       postRepository.save(post);
    }

    public Post get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        return post;
    }
}
