package com.yongcoding.api.service;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.domain.PostEditor;
import com.yongcoding.api.exceptions.PostNotFound;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.request.PostEdit;
import com.yongcoding.api.request.PostSearch;
import com.yongcoding.api.response.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());
        /**
         * Controller -> WebPostService(response를 위한 행위) -> Repository
         *               PostService (외부와 연동, 다른 서비스와 통신, 필요시 레이어 나누는 것도 고려!)
         */
        // 서비스 정책에 맞는 응답 클래스를 분리하는게 좋음
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        // web -> page 1 -> 0
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));

//        return postRepository.findAll(pageable).stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // post.save 대신
    // 알아서 커밋을 침
    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // PostEditorBuilder 생성
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        // 값 설정 후 PostEditor 생성
        PostEditor postEditor = editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        // 엔티티 수정
        post.edit(postEditor);

        // 응답 객체로 반환
        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // 삭제
        postRepository.delete(post);
    }
}
