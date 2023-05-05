package com.dp.hloworld.controller;

import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.*;
import com.dp.hloworld.repository.*;
import com.dp.hloworld.service.CommentService;
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
import java.util.ArrayList;
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
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViewsRepository viewsRepository;

    @Autowired
    private VideoRepository videoRepository;

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
                .comments(commentService.findByVideoId(id))
                .likes(video.getLikes()).build();

        return videoResponse;
    }

    @GetMapping(value="/all")
    public List<VideoResponse> findAllVideo() {
        return videoService.getAllVideo();
    }

    @PostMapping("/view/{videoId}")
    public void viewCount(@PathVariable long videoId,HttpServletRequest request) {
        System.out.print(videoId);
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Optional<Views> optionalViews= viewsRepository.findByUserIdAndVideoId(userOptional.get().getId(),videoId);
        if(optionalViews.isEmpty()){
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
    }

    @GetMapping("/category/{category}")
    public List<VideoResponse> findVideoByCategory (@PathVariable String category){
        List<Video> videoList = videoRepository.findByCategory(category);
        List<VideoResponse> videoResponseList = new ArrayList<>();
        for(Video v:videoList){
            VideoResponse videoResponse = VideoResponse.builder().title(v.getTitle()).id(v.getId()).thumbnailImg(v.getThumbnailImg())
                    .views(v.getViews()).uploader(userRepository.findById(v.getUploaderId()).get()).createdAt(v.getCreatedAt())
                    .build();
            videoResponseList.add(videoResponse);
        }
        return videoResponseList;
    }

    @PostMapping("/like/{videoId}")
    public void likeCount(@PathVariable long videoId,HttpServletRequest request) {
        System.out.print(videoId);
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Likes likes = Likes.builder().videoId(videoId).userId(userOptional.get().getId()).likedAt(Date.valueOf(
                LocalDate.now())).build();
        likeRepository.save(likes);
        Video video = videoService.getVideo(videoId).get();
        long likesCount =video.getLikes();
        likesCount++;
        video.setLikes(likesCount);
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

    @Autowired
    FavouriteRepository favouriteRepository;

    @PostMapping("/fav/new/{videoId}")
    public void addToFav(@PathVariable long videoId,HttpServletRequest request){
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Favourite favourite = Favourite.builder().videoId(videoId).userId(userOptional.get().getId()).build();
        favouriteRepository.save(favourite);
    }

    @GetMapping("fav/all")
    public List<FavouriteResponse> findAllFavVideo(HttpServletRequest request){
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        List<Long> videoIds = favouriteRepository.findByUserId(userOptional.get().getId());
        List<FavouriteResponse> favouriteResponseList = new ArrayList<>();
        if(videoIds.size()>0){
            for(long id : videoIds){
                Optional<Video> video = videoService.getVideo(id);
                FavouriteResponse favouriteResponse = FavouriteResponse.builder().id(video.get().getId()).likes(video.get().getLikes())
                        .thumbnailImg(video.get().getThumbnailImg()).views(video.get().getViews()).title(video.get().getTitle())
                        .uploader(userRepository.findById(video.get().getUploaderId()).get()).updatedAt(video.get().getUpdatedAt()).build();
                favouriteResponseList.add(favouriteResponse);

            }
        }

        return favouriteResponseList;
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
