package com.example.activitymodule.Service;

import com.example.activitymodule.Domain.Comment;
import com.example.activitymodule.Domain.CommentLike;
import com.example.activitymodule.Dto.CreateCommentDto;
import com.example.activitymodule.Repository.CommentLikeRepository;
import com.example.activitymodule.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 댓글 생성
    public void createComment(CreateCommentDto createCommentDto) {
        //User user = userService.getUser(userService.getCurrentUser());
        Comment comment = Comment.builder()
                .userId(createCommentDto.getUserId())
                .content(createCommentDto.getContent())
                .post(postService.getPostById(createCommentDto.getPostId()))
                .build();
        commentRepository.save(comment);
        //applicationEventPublisher.publishEvent(new CommentEvent(this, comment, CommentEvent.EventType.CREATE));

    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if(!comment.getUserId().equals(userId)){
            throw new IllegalArgumentException("해당 댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
        //applicationEventPublisher.publishEvent(new CommentEvent(this, comment, CommentEvent.EventType.DELETE));
    }

    // 댓글 좋아요
    public void likeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if(commentLikeRepository.existsByUserIdAndComment(userId, comment)){
            throw new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
        }
        CommentLike commentLike = CommentLike.builder()
                .userId(userId)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
        //applicationEventPublisher.publishEvent(new CommentLikeEvent(this, commentLike));
    }

}
