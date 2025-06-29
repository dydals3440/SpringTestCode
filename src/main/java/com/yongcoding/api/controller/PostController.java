package com.yongcoding.api.controller;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable(name = "postId") Long id) {
       Post post =  postService.get(id);
       return post;
    }
}
