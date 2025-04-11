package com.nate.news.app.common.util;

import com.nate.news.app.domain.dto.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class NateNewsScraperUtil {

    // 카테고리와 MID 값을 매핑하는 Map 선언
    private static final Map<String, String> categoryToMid = new HashMap<>();

    static {
        categoryToMid.put("", "n0100");  // 전체
        categoryToMid.put("pol", "n0101");  // 정치
        categoryToMid.put("eco", "n0102");  // 경제
        categoryToMid.put("soc", "n0103");  // 사회
        categoryToMid.put("int", "n0104"); // 세계
        categoryToMid.put("its", "n0105"); // IT/과학
        categoryToMid.put("spo", "n0106");  // 스포츠
        categoryToMid.put("ent", "n0107");  // 연예
        categoryToMid.put("col", "n0108");  // 칼럼
        categoryToMid.put("etc", "n0108");  // 모르겠다... 일단 칼럼 준다.
    }

    // 카테고리 값을 받아서 해당하는 MID를 반환하는 메서드
    public static String getMidFromCategory(String category) {
        return categoryToMid.getOrDefault(category, "n0100");
    }

    public List<Article> recentArticleIds(int page, String category) {
        ArrayList<Article> articleVos = new ArrayList<>();

        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        try {
            // Connect to the target URL and parse the HTML
            String url = "https://news.nate.com/recent?mid=" + getMidFromCategory(category) + "&type=c&date=" + formattedDate + "&page=" + page;
            log.info("url [{}]", url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
            String content = doc.html();
            doc = Jsoup.parse(content, "UTF-8");

            Elements articles = doc.select("div.mlt01");

            // Loop through and print the href and text of each link
            for (Element article : articles) {
                Element link = article.selectFirst("a.lt1");
                String linkUrl = link != null ? link.attr("href") : "URL not found";

                // Extract the title (h2 element)
                Element titleElement = article.selectFirst(".tb h2.tit");
                String title = titleElement != null ? titleElement.text() : "Title not found";

                // Extract the body summary (text outside h2 within the span)
                Element spanElement = article.selectFirst(".tb");
                String snippet = spanElement != null ? spanElement.ownText() : "Summary not found";

                Element mediumElement = article.selectFirst(".medium");
                String medium = mediumElement != null ? mediumElement.text() : "Medium not found";

                // 마지막 11자리 (02-21 10:48) 분리
                String createDate = "2025-" + medium.substring(medium.length() - 11) + ":00";
                // 앞부분 (언론사 이름) 분리
                String mediaName = medium.substring(0, medium.length() - 11).trim();

                // img 태그 선택
                Element imgElement = article.selectFirst("img");
                String imgUrl = null;
                // src 속성 추출
                if (imgElement != null) {
                    imgUrl = imgElement.attr("src");
                    // 제거할 부분
                    String prefixToRemove = "thumbnews.nateimg.co.kr/news90///";

                    // 제거 처리
                    imgUrl = imgUrl.replace(prefixToRemove, "");
                }

                // 정규식 패턴 (view와 ?mid 사이의 텍스트 추출)
                String regex = "/view/([a-zA-Z0-9]+)\\?mid";

                // 패턴 컴파일
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(linkUrl);

                // 매칭된 기사 ID 추출
                if (matcher.find()) {
                    String articleId = matcher.group(1);
                    articleVos.add(Article.builder()
                            .itemId(articleId)
                            .title(title)
                            .imageUrl(imgUrl)
                            .linkUrl(linkUrl)
                            .snippet(snippet)
                            .media(mediaName)
                            .mediaName(mediaName)
                            .createDate(createDate)
                            .timeAgo(DateUtil.getTimeAgo(createDate))
                            .build());
                } else {
                    System.out.println("Article ID not found");
                }

            }

        } catch (IOException e) {
            System.err.println("Error while connecting to the website: " + e.getMessage());
        }
        return articleVos;
    }


    public List<Article> rankingArticleIds(int page, String category) {
        ArrayList<Article> articleVos = new ArrayList<>();

        if (category == null || "".equals(category)) {
            category = "all";
        }

        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        // Connect to the target URL and parse the HTML
        String url = "https://news.nate.com/rank/interest?sc=" + category + "&p=day&date=" + formattedDate + "&page=" + page;
        log.info("url [{}]", url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String content = doc.html();

        doc = Jsoup.parse(content, "UTF-8");

        Elements articles = doc.select(".postRankSubjectList .mduSubjectList");

        String title = null;
        String link = null;
        String imageUrl = null;
        String media = null;
        String date = null;

        for (Element article : articles) {
            // 제목 가져오기
            title = article.select(".tb h2.tit").text();
            // 링크 가져오기
            link = article.select("a.lt1").attr("href");
            // 이미지 URL 가져오기
            imageUrl = article.select(".ib .mediatype img").attr("src");
            // 매체사 가져오기
            String medium = article.select(".medium").text().split(" ")[0];  // 첫 번째 단어 (언론사)
            media = medium.substring(0, medium.length() - 10).trim();

            // 날짜 가져오기
            //date = article.select(".medium em").text();
            date = medium.substring(medium.length() - 10) + " 00:00:00";

            String prefixToRemove = "thumbnews.nateimg.co.kr/news90///";
            imageUrl = imageUrl.replace(prefixToRemove, "");

            // 정규식 패턴 (view와 ?mid 사이의 텍스트 추출)
            String regex = "/view/([a-zA-Z0-9]+)\\?mid";

            // 패턴 컴파일
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(link);

            // 매칭된 기사 ID 추출
            if (matcher.find()) {
                String articleId = matcher.group(1);

                Article vo = Article.builder()
                        .itemId(articleId)
                        .title(title)
                        .imageUrl(imageUrl)
                        .linkUrl(link)
                        .snippet(null)
                        .media(media)
                        .mediaName(media)
                        .createDate(date)
                        .timeAgo(DateUtil.getTimeAgo(date))
                        .build();
                articleVos.add(vo);
            }
        }

        imageUrl = null;
        Elements rankSubjects = doc.select(".postRankSubject .mduRankSubject li");

        for (Element item : rankSubjects) {
            // 제목 가져오기
            title = item.select("a h2").text();
            // 링크 가져오기
            link = item.select("a").attr("href");
            // 매체사 가져오기
            media = item.select(".medium").text();
            date = today + " 00:00:00";

            // 정규식 패턴 (view와 ?mid 사이의 텍스트 추출)
            String regex = "/view/([a-zA-Z0-9]+)\\?mid";

            // 패턴 컴파일
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(link);

            // 매칭된 기사 ID 추출
            if (matcher.find()) {
                String articleId = matcher.group(1);

                try {
                    doc = Jsoup.connect("https:" + link)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            .get();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                content = doc.html();
                doc = Jsoup.parse(content, "UTF-8");


                // 모든 img 태그 선택
                Elements imgElements = doc.select("img");

                for (Element img : imgElements) {
                    String imgUrl = img.attr("src");
                    // "thumbnews"가 포함된 이미지 URL만 출력
                    if (imgUrl.contains("thumbnews")) {

                        if (imgUrl.contains("90x67") || imgUrl.contains("140x88")) {
                            imageUrl = null;
                        } else {
                            String prefixToRemove = "thumbnews.nateimg.co.kr/news90///";
                            // 제거 처리
                            imageUrl = imgUrl.replace(prefixToRemove, "");
                            break;
                        }
                    }
                }

                Article vo = Article.builder()
                        .itemId(articleId)
                        .title(title)
                        .imageUrl(imageUrl)
                        .linkUrl(link)
                        .snippet(null)
                        .media(media)
                        .mediaName(media)
                        .createDate(date)
                        .timeAgo(DateUtil.getTimeAgo(date))
                        .build();

                if (imageUrl != null) {
                    articleVos.add(vo);
                }
                imageUrl = null;
            }
        }
        return articleVos;
    }


}