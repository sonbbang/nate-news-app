package com.nate.news.app.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DateUtil {
    public static Date parseDate(String dateStr) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 주어진 날짜 문자열을 "몇일 전", "몇시간 전" 형태로 변환하는 메소드
    public static String getTimeAgo(String dateStr) {
        Date date = null;
        try {
            date = parseDate(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillis = new Date().getTime() - date.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (diffInDays > 0) {
            return diffInDays + "일";
        } else if (diffInHours > 0) {
            return diffInHours + "시간";
        } else if (diffInMinutes > 0) {
            return diffInMinutes + "분";
        } else {
            return diffInSeconds + "초";
        }
    }

}
