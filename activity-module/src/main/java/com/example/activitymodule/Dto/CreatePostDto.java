package com.example.activitymodule.Dto;

import lombok.Data;

@Data
public class CreatePostDto {
    private Long userId;
    private String title;
    private String content;

}
