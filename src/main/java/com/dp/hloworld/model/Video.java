package com.dp.hloworld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "video")
public class Video {

    private static final long serialVersionUID = 5313493413859894403L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private String language;

    @Column(name = "uploader_id")
    private long uploaderId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "thumbnail_image")
    private String thumbnailImg;

    @Column(name = "video_file")
    private String videoFile;

    @Column
    private long views;

    @Column
    private long likes;
}
