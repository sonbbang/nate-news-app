package com.nate.news.app.common.constants;

public class NewsConstants {
    private NewsConstants() {
    } // 인스턴스화 방지

    public static final double SIMILARITY_THRESHOLD = 0.7;

    public static final String BASE_NEWS_URL = "https://news.nate.com/view/";

    public static final class Functions {
        private Functions() {
        }

        public static final String RECENT = "recent";
        public static final String RANKING = "ranking";
    }
}
