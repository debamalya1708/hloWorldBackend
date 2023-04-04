package com.dp.hloworld.controller;

import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.User;
import com.dp.hloworld.model.UserConstants;
import com.dp.hloworld.model.Video;
import com.dp.hloworld.model.VideoResponse;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.service.UserService;
import com.dp.hloworld.service.VideoService;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

        VideoResponse videoResponse =VideoResponse.builder().id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .category(video.getCategory())
                .language(video.getLanguage())
                .uploader(userRepository.findById(video.getUploaderId()).get())
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

}
