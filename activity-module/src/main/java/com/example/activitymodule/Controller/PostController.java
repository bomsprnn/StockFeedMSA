package com.example.activitymodule.Controller;

import com.example.activitymodule.Dto.CreatePostDto;
import com.example.activitymodule.Dto.ViewPostDto;
import com.example.activitymodule.Dto.postReqDto;
import com.example.activitymodule.Service.JWTParseService;
import com.example.activitymodule.Service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/activity/post") // 기본 경로
public class PostController {
    private final PostService postService;

    /**
     * 포스트 작성
     */
    @PostMapping("/create")
    public ResponseEntity<String> createPost(HttpServletRequest request, @RequestBody CreatePostDto createPostDto) {
        log.info("createPostDto: {}", createPostDto);
        postService.createPost(request, createPostDto);
        return ResponseEntity.ok("포스트 작성 완료.");
    }

    /**
     * 포스트 조회
     */
    @GetMapping("/get")
    public ResponseEntity<ViewPostDto> viewPost(HttpServletRequest request, @RequestBody postReqDto postReqDto) {
        ViewPostDto viewPostDto = postService.viewPost(request, postReqDto.getPostId())  ;
        return ResponseEntity.ok(viewPostDto);
    }

    /**
     * 포스트 삭제
     */
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(HttpServletRequest request, @PathVariable Long postId) {
        postService.deletePost(request, postId);
        return ResponseEntity.ok("포스트 삭제 완료.");
    }

    /**
     * 포스트 수정
     */
    // 수정
    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updatePost(HttpServletRequest request, @PathVariable Long postId, @RequestBody CreatePostDto createPostDto) {
        postService.updatePost(request, postId, createPostDto);
        return ResponseEntity.ok("포스트 수정 완료.");
    }

    /**
     * 포스트 좋아요
     */
    @PostMapping("/like/{postId}")
    public ResponseEntity<String> likePost(HttpServletRequest request, @PathVariable Long postId) {
        postService.likePost(request, postId);
        return ResponseEntity.ok("포스트 좋아요 완료.");
    }

    /**
     * 포스트 좋아요 취소
     */
    @DeleteMapping("/like/{postId}")
    public ResponseEntity<String> unlikePost(HttpServletRequest request, @PathVariable Long postId) {
        postService.unlikePost(request, postId);
        return ResponseEntity.ok("포스트 좋아요 취소 완료.");
    }



    // 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
