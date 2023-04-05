package com.dp.hloworld.controller;

import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.*;
import com.dp.hloworld.repository.CommentRepository;
import com.dp.hloworld.repository.LikeRepository;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.repository.ViewsRepository;
import com.dp.hloworld.service.UserService;
import com.dp.hloworld.service.VideoService;
import io.vavr.control.Option;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping(value="/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViewsRepository viewsRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value="/new")
    public void save(@RequestBody Video video, HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);

        video.setUploaderId(userOptional.get().getId());
        video.setCreatedAt(Date.valueOf(LocalDate.now()));
        video.setUpdatedAt(Date.valueOf(LocalDate.now()));
        video.setLikes(0);
        video.setViews(0);
        videoService.saveVideo(video);
    }

    @GetMapping(value="/{id}")
    public VideoResponse findVideo(@PathVariable long id) {

        Video video =  videoService.getVideo(id).get();
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

        return videoResponse;
    }

    @GetMapping(value="/all")
    public List<VideoResponse> findAllVideo() {
        return videoService.getAllVideo();
    }

    @PostMapping("/view")
    public void viewCount(@RequestBody long videoId,HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Views views = Views.builder().videoId(videoId).userId(userOptional.get().getId())
                .viewedAt(Date.valueOf(
                        LocalDate.now())).build();
        viewsRepository.save(views);
        Video video = videoService.getVideo(videoId).get();
        long viewsCount =videoService.getVideo(videoId).get().getViews();
        viewsCount++;
        video.setViews(viewsCount);
        update(video);
    }

    @PostMapping("/like")
    public void likeCount(@RequestParam long videoId,HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Likes likes = Likes.builder().videoId(videoId).userId(userOptional.get().getId()).likedAt(Date.valueOf(
                LocalDate.now())).build();
        likeRepository.save(likes);
        Video video = videoService.getVideo(videoId).get();
        long likesCount =video.getLikes();
        likesCount++;
        video.setViews(likesCount);
        update(video);
    }

    @PostMapping("/comment/new")
    public void newComment(@RequestBody CommentRequest commentRequest,HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Comment comment = Comment.builder().videoId(commentRequest.getVideoId()).userId(userOptional.get().getId())
                .comments(commentRequest.getComment()).createdAt(Date.valueOf(LocalDate.now())).build();
        commentRepository.save(comment);
    }

    @PutMapping("/update")
    public Video update(@RequestBody Video video) {
        return videoService.saveVideo(video);
    }

    public String getContact(HttpServletRequest request) {
        String contact="";
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            if(!jwtUtil.isTokenExpired(jwtToken)){
                Map<String, String> map = jwtUtil.getJwtTokenDetails(request);
                contact= map.get(UserConstants.contactNo);
            }
        }
        return contact;
    }

    @Data
    static class CommentRequest{
        String comment;
        long videoId;
    }

}
