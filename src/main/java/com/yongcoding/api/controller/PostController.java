package com.yongcoding.api.controller;

import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.request.PostEdit;
import com.yongcoding.api.request.PostSearch;
import com.yongcoding.api.response.PostResponse;
import com.yongcoding.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
//        if (request.getTitle().contains("바보")) {
//            throw new InvalidRequest();
//        }
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // Request 클래스 -> PostCreate (요청과, Validation을 할 수 있는 정책을 담아둔 클래스)
        // Response 클래스 -> PostResponse (서비스 정책에 맞는 로직이 들어갈 수 있는 클래스)
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
