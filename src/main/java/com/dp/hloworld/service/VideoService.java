package com.dp.hloworld.service;

import com.dp.hloworld.model.User;
import com.dp.hloworld.model.Video;
import com.dp.hloworld.model.VideoResponse;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public Video saveVideo(Video video){
        return videoRepository.save(video);
    }

    public Optional<Video> getVideo(long id){
        return videoRepository.findById(id);
    }

    public List<VideoResponse> getAllVideo(){

        List<Video> videos =  videoRepository.findAll();
        List<VideoResponse> videoResponses = new ArrayList<>();
        for (Video video:videos) {
            User uploader = userRepository.findById(video.getUploaderId()).get();
            uploader.setPassword("");
            VideoResponse videoResponse =VideoResponse.builder().id(video.getId())
                    .title(video.getTitle())
                    .description(video.getDescription())
                    .category(video.getCategory())
                    .language(video.getLanguage())
                    .uploader(uploader)
                    .createdAt(video.getCreatedAt())
                    .updatedAt(video.getUpdatedAt())
                    .thumbnailImg(video.getThumbnailImg())
                    .videoFile(video.getVideoFile())
                    .views(video.getViews())
                    .likes(video.getLikes()).build();
            videoResponses.add(videoResponse);
        }
        return videoResponses;
    }
}
