package com.yongcoding.api.service;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    // Field Injection (Autowired)는 테스트 케이스 이외의 상황에서는 비선호.
    // 생성자 인젝션을 활용해서 PostRepository를 주입받는다.
    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        postRepository.save(post);
    }
}
