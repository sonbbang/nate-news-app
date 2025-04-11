package com.nate.news.app.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Builder
@Data
public class Article {

    private String category;

    @JsonProperty("createDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createDate;
    private String imageUrl;
    private String itemId;

    private String media;
    private String title;
    private String linkUrl;
    private String snippet;

    private String mediaName;
    private String timeAgo;

}
