package com.dp.hloworld.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class VideoResponse {

    private long id;

    private String title;

    private String description;

    private String category;

    private String language;

    private User uploader;

    private Date createdAt;

    private Date updatedAt;

    private String thumbnailImg;

    private String videoFile;

    private long views;

    private long likes;
}
