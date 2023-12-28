package com.project.firstTry.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String first_name;

    private  String last_name;

    private String email;

    private String password;

    private String phone;
}
