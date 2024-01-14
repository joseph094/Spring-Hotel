package com.project.firstTry.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private long rate;
    private String content;
    private long userId; 
}
