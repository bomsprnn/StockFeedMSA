package com.example.usermodule.Dto.FeignDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserResponseDto {
    private String name;
    private String email;

    @Builder
    public UserResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
