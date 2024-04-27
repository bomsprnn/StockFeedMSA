package com.example.usermodule.Dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String password;
    private String newPassword;
}
