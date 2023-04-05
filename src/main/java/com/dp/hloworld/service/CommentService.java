package com.dp.hloworld.service;

import com.dp.hloworld.model.Comment;
import com.dp.hloworld.model.CommentResponse;
import com.dp.hloworld.repository.CommentRepository;
import com.dp.hloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CommentResponse> findByVideoId(long id){
        List<Comment> commentList = commentRepository.findByVideoId(id);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : commentList){
            CommentResponse commentResponse = CommentResponse.builder().comment(comment.getComments())
                    .createdAt(comment.getCreatedAt().toString()).commentBy(userRepository.findById(comment.getUserId())
                            .get().getName()).build();

            commentResponseList.add(commentResponse);
        }
        return commentResponseList;
    }
}
