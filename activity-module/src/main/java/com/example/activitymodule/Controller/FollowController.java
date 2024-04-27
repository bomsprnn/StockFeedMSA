package com.example.activitymodule.Controller;

import com.example.activitymodule.Dto.FollowRequestDto;
import com.example.activitymodule.Service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/activity/follow") // 기본 경로
public class FollowController {
    private final FollowService followService;

    /**
     * 팔로우
     */
    @PostMapping("/follow")
    public ResponseEntity<String> follow(HttpServletRequest request, @RequestBody FollowRequestDto followRequestDto) {
        followService.follow(followRequestDto.getToUserId(), request);
        return ResponseEntity.ok("팔로우 완료.");
    }

    /**
     * 언팔로우
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollow(HttpServletRequest request, @RequestBody FollowRequestDto followRequestDto) {
        followService.unfollow(followRequestDto.getToUserId(), request);
        return ResponseEntity.ok("언팔로우 완료.");
    }

    /**
     * 팔로우 여부 확인
     */
    @GetMapping("/isFollowing")
    public ResponseEntity<Boolean> isFollowing(HttpServletRequest request, @RequestBody FollowRequestDto followRequestDto) {
        return ResponseEntity.ok(followService.isFollowing(request, followRequestDto.getToUserId()));
    }

    /**
     * 사용자가 팔로우하는 사용자 목록 조회
     */
    @GetMapping("/following")
    public void followingList(HttpServletRequest request) {
        followService.getFollowings(request);
    }

    /**
     * 사용자를 팔로우하는 사용자 목록 조회
     */
    @GetMapping("/follower")
    public void followerList(HttpServletRequest request) {
        followService.getFollowers(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
