package com.dp.hloworld.repository;

import com.dp.hloworld.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video,Long> {

    List<Video> findVideoByUploaderId(long id);
}
