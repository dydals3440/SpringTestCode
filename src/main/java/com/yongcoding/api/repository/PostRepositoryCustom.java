package com.yongcoding.api.repository;

import com.yongcoding.api.domain.Post;
import com.yongcoding.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
