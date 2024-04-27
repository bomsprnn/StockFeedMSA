package com.example.activitymodule.Controller;

import com.example.activitymodule.Dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userServiceClient", url = "localhost:8081")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    UserResponseDto getUser(@PathVariable("userId") Long userId);

}