package com.yongcoding.api.controller;

import com.yongcoding.api.request.PostCreate;
import com.yongcoding.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public Map<String, String> getPosts(@RequestBody @Valid PostCreate request) {
        postService.write(request);
        return Map.of();
    }
}
