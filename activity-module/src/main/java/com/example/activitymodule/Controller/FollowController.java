package com.example.activitymodule.Controller;

import com.example.activitymodule.Dto.FollowRequestDto;
import com.example.activitymodule.Service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user") // 기본 경로
public class FollowController {
    private final FollowService followService;

//    /**
//     * 팔로우
//     */
//    @PostMapping("/follow")
//    public ResponseEntity<String> follow(@RequestBody FollowRequestDto followRequestDto) {
//        followService.follow(followRequestDto.getToUserId(), followRequestDto.getFromUserId();
//        return ResponseEntity.ok("팔로우 완료.");
//    }
//
//    /**
//     * 언팔로우
//     */
//    @DeleteMapping("/unfollow")
//    public ResponseEntity<String> unfollow(@RequestBody FollowRequestDto followRequestDto) {
//        followService.unfollow(followRequestDto.getToUserId());
//        return ResponseEntity.ok("언팔로우 완료.");
//    }

//    /**
//     * 사용자가 팔로우하는 사용자 목록 조회
//     */
//    @GetMapping("/following")
//    public void followingList(Long userId) {
//        followService.getFollowings(userId);
//    }
//
//    /**
//     * 사용자를 팔로우하는 사용자 목록 조회
//     */
//    @GetMapping("/follower")
//    public void followerList(Long userId) {
//        followService.getFollowers(userId);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
