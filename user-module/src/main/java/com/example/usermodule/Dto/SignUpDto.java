package com.example.usermodule.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SignUpDto {

    private String email;
    private String password;
    private String name;
    private String profileImage;
    private String profileText;

    @Builder
    public SignUpDto(String email, String password, String name, String profileImage, String profileText) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.profileText = profileText;
    }

}
