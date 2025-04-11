package com.nate.news.app.domain.service;

import com.nate.news.app.common.constants.Media;
import com.nate.news.app.common.constants.NewsConstants;
import com.nate.news.app.common.util.DateUtil;
import com.nate.news.app.common.util.JaccardSimilarityUtil;
import com.nate.news.app.domain.adaptor.NewsApiAdapter;
import com.nate.news.app.domain.dto.Article;
import com.nate.news.app.domain.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

import static com.nate.news.app.common.constants.NewsConstants.BASE_NEWS_URL;
import static com.nate.news.app.common.constants.NewsConstants.SIMILARITY_THRESHOLD;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {
    private final NewsApiAdapter newsApiAdapter;

    public NewsResponse getNewsByCategory(String function, String category, int page) {
        NewsResponse newsResponse;

        if (NewsConstants.Functions.RECENT.equals(function)) {
            newsResponse = newsApiAdapter.recent(page, category);
        } else {
            newsResponse = newsApiAdapter.rank(page, category);
        }

        // 중복 제거
        if (NewsConstants.Functions.RECENT.equals(function)) {
            List<Article> articles = newsResponse.getArticles();

            double threshold = SIMILARITY_THRESHOLD;
            List<Article> filteredArticles = JaccardSimilarityUtil.filterDuplicateArticles(articles, threshold);
            newsResponse.setArticles(filteredArticles);
        }

        for (Article article : newsResponse.getArticles()) {
            article.setLinkUrl(BASE_NEWS_URL + article.getItemId());
            article.setMediaName(Media.getByCode(article.getMedia()) == null ? article.getMedia() : Media.getByCode(article.getMedia()).getName());
            article.setTimeAgo(DateUtil.getTimeAgo(article.getCreateDate()));
        }

        // 이미지 없으면 제거
        Iterator<Article> iterator = newsResponse.getArticles().iterator();
        while (iterator.hasNext()) {
            Article articleVo = iterator.next();
            if (articleVo.getImageUrl() == null) {
                iterator.remove(); // null인 imageUrl을 가진 article 삭제
            }
        }

        return newsResponse;
    }
}
