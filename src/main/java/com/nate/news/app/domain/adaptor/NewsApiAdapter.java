package com.nate.news.app.domain.adaptor;

import com.nate.news.app.common.util.NateNewsScraperUtil;
import com.nate.news.app.domain.dto.Article;
import com.nate.news.app.domain.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsApiAdapter {

    private final NateNewsScraperUtil nateNewsScraperUtil;

    public NewsResponse recent(int page, String category) {

        List<Article> articles = nateNewsScraperUtil.recentArticleIds(page, category);

        NewsResponse newsResponse = NewsResponse.builder()
                .articles(articles)
                .build();

        return newsResponse;
    }

    public NewsResponse rank(int page, String category) {

        List<Article> articles = nateNewsScraperUtil.rankingArticleIds(page, category);

        NewsResponse newsResponse = NewsResponse.builder()
                .articles(articles)
                .build();

        return newsResponse;
    }


}
