package com.nate.news.app.controller;

import com.nate.news.app.TestDataFactory;
import com.nate.news.app.domain.dto.NewsResponse;
import com.nate.news.app.domain.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsApiController.class)
@Import(NewsApiControllerTest.MockServiceConfig.class)
class NewsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsService newsService;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public NewsService newsService() {
            return mock(NewsService.class);
        }
    }
    private void givenNewsResponse(NewsResponse response) {
        when(newsService.getNewsByCategory(anyString(), anyString(), anyInt()))
                .thenReturn(response);
    }

    @Test
    @DisplayName("뉴스 목록을 정상적으로 조회한다")
    void shouldReturnNewsList() throws Exception {
        givenNewsResponse(TestDataFactory.createMockNewsResponse());

        mockMvc.perform(get("/api/v1/news")
                        .param("function", "ranking")
                        .param("category", "all")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles").isArray())
                .andExpect(jsonPath("$.articles[0].title").exists())
                .andExpect(jsonPath("$.articles[0].linkUrl").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("페이지 번호가 없는 경우 기본값 1로 조회된다")
    void shouldUseDefaultPageWhenPageIsMissing() throws Exception {
        givenNewsResponse(TestDataFactory.createMockNewsResponse());

        mockMvc.perform(get("/api/v1/news")
                        .param("function", "ranking")
                        .param("category", "all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("빈 결과를 정상적으로 처리한다")
    void shouldHandleEmptyNewsResponse() throws Exception {
        givenNewsResponse(TestDataFactory.createEmptyNewsResponse());

        mockMvc.perform(get("/api/v1/news")
                        .param("function", "ranking")
                        .param("category", "all")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles").isArray())
                .andExpect(jsonPath("$.articles").isEmpty())
                .andDo(print());
    }
}
