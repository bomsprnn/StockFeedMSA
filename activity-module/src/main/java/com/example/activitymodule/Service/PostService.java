package com.example.activitymodule.Service;

import com.example.activitymodule.Controller.UserServiceClient;
import com.example.activitymodule.Domain.Post;
import com.example.activitymodule.Domain.PostLike;
import com.example.activitymodule.Dto.CommentDto;
import com.example.activitymodule.Dto.CreatePostDto;
import com.example.activitymodule.Dto.ViewPostDto;
import com.example.activitymodule.Repository.CommentLikeRepository;
import com.example.activitymodule.Repository.PostLikeRepository;
import com.example.activitymodule.Repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserServiceClient userServiceClient;
    private final JWTParseService jwtParseService;
    private final NewsFeedService newsFeedService;

    // 게시글 생성
    public void createPost(HttpServletRequest request, CreatePostDto createPostDto) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        log.info("userId: {}", userId);
        Post post = Post.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .userId(userId)
                .viewCount(0)
                .build();
        postRepository.save(post);
        newsFeedService.createPostonNewsFeed(userId, post.getUserId(), post.getId());
    }

    // 게시글 수정
    public void updatePost(HttpServletRequest request, Long postId, CreatePostDto createPostDto) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        Post post = checkAndGetPost(postId, createPostDto.getUserId());
        //applicationEventPublisher.publishEvent(new PostEvent(this, post, PostEvent.EventType.DELETE));
        post.update(createPostDto.getTitle(), createPostDto.getContent());
        //applicationEventPublisher.publishEvent(new PostEvent(this, post, PostEvent.EventType.CREATE));
    }

    // 게시글 삭제
    public void deletePost(HttpServletRequest request, Long postId) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        Post post = checkAndGetPost(userId, postId);
        postRepository.delete(post);
        //applicationEventPublisher.publishEvent(new PostEvent(this, post, PostEvent.EventType.DELETE));
    }

    // 게시글 조회및 권한 체크
    private Post checkAndGetPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자에게만 권한이 있습니다.");
        }
        return post;
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    // 게시글 상세 조회
    public ViewPostDto viewPost(HttpServletRequest request, Long postId) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        log.info("사용자 아이디 userId: {}", userId);
        String username = jwtParseService.getUsernameFromToken(request);
        log.info("사용자 이메일 username: {}", username);
        Post post = getPostById(postId);
        post.addViewCount(); // 조회수 증가
        List<CommentDto> commentDtos = fetchCommentsForPost(post, userId);
        int like = postLikeRepository.countByPost(post);
        boolean isLiked = postLikeRepository.existsByUserIdAndPost(userId, post);
        //String username = userServiceClient.getUser(post.getUserId()).getName();
        return ViewPostDto.builder()
                .comments(commentDtos)
                .title(post.getTitle())
                .content(post.getContent())
                .author(username)
                .date(post.getCreatedAt().toString())
                .viewCount(post.getViewCount())
                .likeCount(like)
                .isLiked(isLiked)
                .build();
    }

    // 포스트 좋아요
    public void likePost(HttpServletRequest request, Long postId) {
        Post post = getPostById(postId);
        Long userId = jwtParseService.getUserIdFromToken(request);
        if (postLikeRepository.existsByUserIdAndPost(userId, post)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }
        PostLike postLike = PostLike.builder()
                .userId(userId)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
        //applicationEventPublisher.publishEvent(new PostLikeEvent(this, postLike,null, null,PostLikeEvent.EventType.CREATE));
    }

    // 포스트 좋아요 취소
    public void unlikePost(HttpServletRequest request, Long postId) {
        Post post = getPostById(postId);
        Long userId = jwtParseService.getUserIdFromToken(request);
        if (!postLikeRepository.existsByUserIdAndPost(userId, post)) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다.");
        }
        postLikeRepository.deleteByUserIdAndPost(userId, post);
        //applicationEventPublisher.publishEvent(new PostLikeEvent(this, null, post, user, PostLikeEvent.EventType.DELETE));
    }

    // 포스트 댓글 가져오기
    private List<CommentDto> fetchCommentsForPost(Post post, Long userId) {
        return post.getComment().stream().map(comment -> {
            boolean isLiked = commentLikeRepository.existsByUserIdAndComment(userId, comment);
            int likeCount = commentLikeRepository.countByComment(comment);
            String username = userServiceClient.getUser(comment.getUserId()).getName();
            return new CommentDto(
                    comment.getId(),
                    comment.getContent(),
                    //get name mwthod
                    username,
                    comment.getCreatedAt(),
                    likeCount,
                    isLiked
            );
        }).collect(Collectors.toList());
    }


}
