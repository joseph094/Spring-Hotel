package com.project.firstTry.dto;

import lombok.Data;

@Data
public class UpdatePassword {
    private String email;

    private String newpassword;
}
