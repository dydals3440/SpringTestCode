package com.yongcoding.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongcoding.api.domain.Post;
import com.yongcoding.api.repository.PostRepository;
import com.yongcoding.api.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// host: localhost:8080 ascii-docs 바꾸는법
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yongcoding.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ObjectMapper objectMapper;
    // BeforeEach는 MockMvc를 초기화하는데 사용됩니다. 그냥 주석하고, 어노테이션을 달자
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }

    @Test
    @DisplayName("글 단건 조회 테스트")
    void test1() throws Exception {
        // given
        Post post = Post.builder().title("123456789").content("bar").build();
        postRepository.save(post);

        // expected
        ResultActions resultActions = this.mockMvc.perform(get("/posts/{postId}", 1L).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andDo(document("index", pathParameters(parameterWithName("postId").description("게시글 ID")), responseFields(fieldWithPath("id").description("게시글 ID"), fieldWithPath("title").description("제목"), fieldWithPath("content").description("내용"))));
    }

    @Test
    @DisplayName("글 등록 테스트")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
            .title("나는 매튜.")
            .content("서울을 삽니다")
            .build();

        String json = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/posts")
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(json)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("index",
                requestFields(
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용")
                )
            ));
    }


}
