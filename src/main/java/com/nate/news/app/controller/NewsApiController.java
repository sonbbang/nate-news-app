package com.nate.news.app.controller;

import com.nate.news.app.domain.dto.NewsResponse;
import com.nate.news.app.domain.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewsApiController {

    private final NewsService newsService;

    @GetMapping({"/news"})
    public NewsResponse getNewsList(@RequestParam(defaultValue = "recent") String function,
                                   @RequestParam(defaultValue = "all") String category,
                                   @RequestParam(defaultValue = "1") int page) {
        return newsService.getNewsByCategory(function, category, page);
    }


}
