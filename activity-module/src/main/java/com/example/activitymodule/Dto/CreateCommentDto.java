package com.example.activitymodule.Dto;

import lombok.Data;

@Data
public class CreateCommentDto {
    private Long userId;
    private Long postId;
    private String content;
}
