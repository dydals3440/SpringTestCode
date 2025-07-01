package com.yongcoding.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongcoding.api.domain.Post;
import com.yongcoding.api.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yongcoding.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
       return jpaQueryFactory.selectFrom(post)
               .limit(postSearch.getSize())
               .offset(postSearch.getOffset())
               .orderBy(post.id.desc())
               .fetch();
    }
}
