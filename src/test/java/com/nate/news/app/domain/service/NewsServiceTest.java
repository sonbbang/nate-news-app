package com.nate.news.app.domain.service;

import com.nate.news.app.TestDataFactory;
import com.nate.news.app.domain.adaptor.NewsApiAdapter;
import com.nate.news.app.domain.dto.Article;
import com.nate.news.app.domain.dto.NewsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsApiAdapter newsApiAdapter;

    @Test
    @DisplayName("랭킹 뉴스를 정상적으로 조회한다")
    void getRankingNews() {
        // given
        String function = "ranking";
        String category = "all";
        int page = 1;

        List<Article> mockArticles = TestDataFactory.createMockArticles();
        NewsResponse mockResponse = NewsResponse.builder()
                .articles(mockArticles)
                .build();

        when(newsApiAdapter.rank(page, category))
                .thenReturn(mockResponse);

        // when
        NewsResponse result = newsService.getNewsByCategory(function, category, page);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getArticles()).hasSize(mockArticles.size());
        verify(newsApiAdapter).rank(page, category);
    }

    @Test
    @DisplayName("최신 뉴스 조회시 중복 기사를 제거한다")
    void getRecentNewsRemoveDuplicates() {
        // given
        String function = "recent";
        String category = "all";
        int page = 1;

        List<Article> mockArticles = TestDataFactory.createDuplicateMockArticles();
        NewsResponse mockResponse = NewsResponse.builder()
                .articles(mockArticles)
                .build();

        when(newsApiAdapter.recent(page, category))
                .thenReturn(mockResponse);

        // when
        NewsResponse result = newsService.getNewsByCategory(function, category, page);

        // then
        assertThat(result.getArticles()).hasSizeLessThan(mockArticles.size());
        verify(newsApiAdapter).recent(page, category);
    }

    @Test
    @DisplayName("빈 결과를 정상적으로 처리한다")
    void handleEmptyResult() {
        // given
        String function = "ranking";
        String category = "all";
        int page = 1;

        NewsResponse emptyResponse = NewsResponse.builder()
                .articles(List.of())
                .build();

        when(newsApiAdapter.rank(page, category))
                .thenReturn(emptyResponse);

        // when
        NewsResponse result = newsService.getNewsByCategory(function, category, page);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getArticles()).isEmpty();
    }
}