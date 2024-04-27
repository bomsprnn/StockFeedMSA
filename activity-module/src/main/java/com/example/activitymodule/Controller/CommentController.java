package com.example.activitymodule.Controller;

import com.example.activitymodule.Dto.CommentDto;
import com.example.activitymodule.Dto.CreateCommentDto;
import com.example.activitymodule.Service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/activity/post/comment") // 기본 경로
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("test");
    }
    /**
     * 댓글 작성
     */
    @PostMapping("/create")
    public ResponseEntity<String> createComment(HttpServletRequest request, @RequestBody CreateCommentDto createCommentDto) {
        commentService.createComment(request, createCommentDto);
        return ResponseEntity.ok().body("댓글 작성 완료.");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment(HttpServletRequest request, @RequestBody CommentDto commentId) {
        commentService.deleteComment(request, commentId.getId());
        return ResponseEntity.ok().body("댓글 삭제 완료.");
    }

    /**
     * 댓글 좋아요
     */
    @PostMapping("/like")
    public ResponseEntity<String> likeComment(HttpServletRequest request, @RequestBody CommentDto commentId) {
        commentService.likeComment(request, commentId.getId());
        return ResponseEntity.ok().body("댓글 좋아요 완료.");
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
