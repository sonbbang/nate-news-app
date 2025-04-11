package com.nate.news.app.domain.service;

import com.nate.news.app.domain.dto.NewsResponse;


public interface NewsService {
    NewsResponse getNewsByCategory(String function, String category, int page);
}
