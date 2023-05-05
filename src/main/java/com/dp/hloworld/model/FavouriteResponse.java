package com.dp.hloworld.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class FavouriteResponse {

    private long id;

    private String title;

    private User uploader;

    private Date createdAt;

    private Date updatedAt;

    private String thumbnailImg;

    private long views;

    private long likes;

}
