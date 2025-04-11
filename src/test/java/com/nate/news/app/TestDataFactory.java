package com.nate.news.app;

import com.nate.news.app.domain.dto.Article;
import com.nate.news.app.domain.dto.NewsResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataFactory {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Article createArticle(String title, String content) {
        return Article.builder()
                .title(title)
                .snippet(content)
                .itemId(UUID.randomUUID().toString())
                .createDate(LocalDateTime.now().format(DATE_FORMATTER))
                .category("all")
                .media("nate")
                .mediaName("네이트뉴스")
                .imageUrl("https://example.com/image.jpg")
                .linkUrl("https://news.nate.com/view/" + UUID.randomUUID().toString())
                .timeAgo("1시간 전")
                .build();
    }

    public static List<Article> createMockArticles() {
        return Arrays.asList(
                createArticle("첫 번째 뉴스 제목", "첫 번째 뉴스 내용입니다."),
                createArticle("두 번째 뉴스 제목", "두 번째 뉴스 내용입니다."),
                createArticle("세 번째 뉴스 제목", "세 번째 뉴스 내용입니다.")
        );
    }

    public static List<Article> createDuplicateMockArticles() {
        return Arrays.asList(
                createArticle("동일한 뉴스 제목", "좀 길어야 하네요. 첫번째 비슷한 내용의 뉴스입니다."),
                createArticle("동일한 뉴스 제목", "좀 길어야 하네요. 두번째 비슷한 내용의 뉴스입니다."),
                createArticle("전혀 다른 뉴스 제목", "전혀 다른 내용의 뉴스입니다.")
        );
    }

    public static NewsResponse createMockNewsResponse() {
        return NewsResponse.builder()
                .articles(createMockArticles())
                .build();
    }

    public static NewsResponse createEmptyNewsResponse() {
        return NewsResponse.builder()
                .articles(Arrays.asList())
                .build();
    }
}