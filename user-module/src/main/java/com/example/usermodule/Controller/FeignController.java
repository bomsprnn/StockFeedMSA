package com.example.usermodule.Controller;

import com.example.usermodule.Domain.User;
import com.example.usermodule.Dto.FeignDto.UserResponseDto;
import com.example.usermodule.Repository.UserRepository;
import com.example.usermodule.Service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FeignController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        UserResponseDto userResponseDto = userService.userToResponseDto(user);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponseDto);
    }


}
