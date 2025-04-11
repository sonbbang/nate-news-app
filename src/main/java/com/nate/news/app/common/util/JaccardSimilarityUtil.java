package com.nate.news.app.common.util;

import com.nate.news.app.domain.dto.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class JaccardSimilarityUtil {
    // 2-gram을 생성하는 메서드
    public static Set<String> generateNGrams(String text, int n) {
        Set<String> ngrams = new HashSet<>();
        text = text.replaceAll("\\s+", ""); // 공백 제거
        for (int i = 0; i <= text.length() - n; i++) {
            ngrams.add(text.substring(i, i + n));
        }
        return ngrams;
    }

    // 문장을 단어 단위로 Set으로 변환
    public static Set<String> getWordSet(String text) {
        return new HashSet<>(Arrays.asList(text.split("\\s+"))); // 띄어쓰기 기준 단어 분리
    }

    // Jaccard Similarity 계산 (단어 단위)
    public static double calculateJaccardSimilarityWord(String str1, String str2) {

        if (str1 == null || str2 == null) {
            return 0;
        }

        Set<String> words1 = getWordSet(str1);
        Set<String> words2 = getWordSet(str2);

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2); // 교집합

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2); // 합집합

        return (double) intersection.size() / union.size();
    }

    // Jaccard Similarity 계산 메서드
    public static double calculateJaccardSimilarity(String str1, String str2) {
        Set<String> ngrams1 = generateNGrams(str1, 2);
        Set<String> ngrams2 = generateNGrams(str2, 2);

        Set<String> intersection = new HashSet<>(ngrams1);
        intersection.retainAll(ngrams2); // 교집합

        Set<String> union = new HashSet<>(ngrams1);
        union.addAll(ngrams2); // 합집합

        return (double) intersection.size() / union.size();
    }

    public static List<Article> filterDuplicateArticles(List<Article> articles, double threshold) {
        Set<Integer> duplicateIndexes = new HashSet<>();
        List<Article> uniqueArticles = new ArrayList<>();

        for (int i = 0; i < articles.size(); i++) {
            if (duplicateIndexes.contains(i)) continue; // 이미 중복으로 판정된 기사 스킵

            // 기사들끼리 비교
            for (int j = i + 1; j < articles.size(); j++) {
                if (duplicateIndexes.contains(j)) continue; // 이미 중복이면 스킵

                double similarity = calculateJaccardSimilarityWord(articles.get(i).getSnippet(), articles.get(j).getSnippet());

                if (similarity >= threshold) {
                    duplicateIndexes.add(j); // 🔥 articles 내의 인덱스 저장
                    log.info("추천 기사 중복 index [{}] - [{}] similarity [{}]", i, j, similarity);
                }
            }

            //  중복이 아닌 경우만 추가
            if (!duplicateIndexes.contains(i)) {
                uniqueArticles.add(articles.get(i));
            }
        }

        log.info("최종 중복 제거 후 기사 개수: [{}] -> [{}]", articles.size(), uniqueArticles.size());
        return uniqueArticles;
    }

}
