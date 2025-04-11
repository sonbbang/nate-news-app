package com.nate.news.app.domain.adaptor;

import com.nate.news.app.domain.dto.NewsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class NewsApiAdapterTest {

    @Autowired
    private NewsApiAdapter newsApiAdapter;

    @Test
    @DisplayName("랭킹 뉴스를 정상적으로 가져온다")
    void getRankingNews() {
        // given
        int page = 1;
        String category = "all";

        // when
        NewsResponse response = newsApiAdapter.rank(page, category);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getArticles()).isNotEmpty();
    }

    @Test
    @DisplayName("최신 뉴스를 정상적으로 가져온다")
    void getRecentNews() {
        // given
        int page = 1;
        String category = "all";

        // when
        NewsResponse response = newsApiAdapter.recent(page, category);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getArticles()).isNotEmpty();
    }

}