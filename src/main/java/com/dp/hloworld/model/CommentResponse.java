package com.dp.hloworld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {

    String commentBy;
    String comment;
    String createdAt;
}
