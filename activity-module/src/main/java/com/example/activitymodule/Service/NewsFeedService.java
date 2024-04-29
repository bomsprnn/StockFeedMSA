package com.example.activitymodule.Service;

import com.example.activitymodule.Dto.NewsFeedDto;
import com.example.activitymodule.Repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NewsFeedService {
    private final FollowRepository followRepository;
    private final KafkaNewsFeedProducer kafkaNewsFeedProducer;

    private NewsFeedDto createNewsFeed(Long userId, Long ownerId, String type, Long followUserId, Long postId, Long commentId) {
        NewsFeedDto newsFeed = NewsFeedDto.builder()
                .userId(userId) // 뉴스피드를 볼 사용자
                .ownUserId(ownerId) // 뉴스피드를 생성한 사용자
                .type(type) // 뉴스피드 타입
                .followUserId(followUserId) // ownuser가 팔로우한 사용자
                .postId(postId)
                .commentId(commentId)
                .build();
        return newsFeed;
    }

    public void createPostonNewsFeed(Long userId, Long ownerId, Long postId) {
        List<Long> followers = followRepository.findByFollowingId(ownerId)
                .stream().map(follow -> follow.getFollowerId()).collect(Collectors.toList());
        // 게시물 생성자의 팔로워들 리스트
        followers.forEach(followerId -> {
            if (!followerId.equals(ownerId)) { // 게시물 생성자는 뉴스피드에 생성하지 않음
                NewsFeedDto newsFeed = createNewsFeed(followerId, ownerId, "POST", null, postId, null);
                kafkaNewsFeedProducer.sendCreateNewsFeedMessage(newsFeed);
                log.info("Post NewsFeed Created");
            }
        });
    }

    public void createCommentonNewsFeed(Long userId, Long ownerId, Long commentId) {
        List<Long> followers = followRepository.findByFollowingId(ownerId)
                .stream().map(follow -> follow.getFollowerId()).collect(Collectors.toList());
        // 댓글 생성자의 팔로워들 리스트
        followers.forEach(followerId -> {
            if (!followerId.equals(ownerId)) { // 댓글 생성자는 뉴스피드에 생성하지 않음
                NewsFeedDto newsFeed = createNewsFeed(followerId, ownerId, "COMMENT", null, null, commentId);
                kafkaNewsFeedProducer.sendCreateNewsFeedMessage(newsFeed);
                log.info("Comment NewsFeed Created");
            }
        });
    }

    public void createFollowonNewsFeed(Long userId, Long ownerId, Long followUserId) {
        List<Long> followers = followRepository.findByFollowingId(ownerId)
                .stream().map(follow -> follow.getFollowerId()).collect(Collectors.toList());
        // 팔로우 생성자의 팔로워들 리스트
        followers.forEach(followerId -> {
            if (!followerId.equals(ownerId)) { // 팔로우 생성자는 뉴스피드에 생성하지 않음
                NewsFeedDto newsFeed = createNewsFeed(followerId, ownerId, "FOLLOW", followUserId, null, null);
                kafkaNewsFeedProducer.sendCreateNewsFeedMessage(newsFeed);
                log.info("Follow NewsFeed Created");
            }
        });
    }

    public void createCommentLikeonNewsFeed(Long userId, Long ownerId, Long commentId) {
        List<Long> followers = followRepository.findByFollowingId(ownerId)
                .stream().map(follow -> follow.getFollowerId()).collect(Collectors.toList());
        // 댓글 좋아요 생성자의 팔로워들 리스트
        followers.forEach(followerId -> {
            if (!followerId.equals(ownerId)) { // 댓글 좋아요 생성자는 뉴스피드에 생성하지 않음
                NewsFeedDto newsFeed = createNewsFeed(followerId, ownerId, "COMMENTLIKE", null, null, commentId);
                kafkaNewsFeedProducer.sendCreateNewsFeedMessage(newsFeed);
                log.info("CommentLike NewsFeed Created");
            }
        });
    }

    public void createPostLikeonNewsFeed(Long userId, Long ownerId, Long postId) {
        List<Long> followers = followRepository.findByFollowingId(ownerId)
                .stream().map(follow -> follow.getFollowerId()).collect(Collectors.toList());
        // 게시물 좋아요 생성자의 팔로워들 리스트
        followers.forEach(followerId -> {
            if (!followerId.equals(ownerId)) { // 게시물 좋아요 생성자는 뉴스피드에 생성하지 않음
                NewsFeedDto newsFeed = createNewsFeed(followerId, ownerId, "POSTLIKE", null, postId, null);
                kafkaNewsFeedProducer.sendCreateNewsFeedMessage(newsFeed);
                log.info("PostLike NewsFeed Created");
            }
        });
    }

    public void deletePostonNewsFeed(Long ownerId, Long postId) {
        NewsFeedDto newsFeed = createNewsFeed(null, ownerId, "POST", null, postId, null);
        kafkaNewsFeedProducer.sendDeleteNewsFeedMessage(newsFeed);
        log.info("Post NewsFeed Deleted");
    }

    public void deleteCommentonNewsFeed(Long ownerId, Long commentId) {
        NewsFeedDto newsFeed = createNewsFeed(null, ownerId, "COMMENT", null, null, commentId);
        kafkaNewsFeedProducer.sendDeleteNewsFeedMessage(newsFeed);
        log.info("Comment NewsFeed Deleted");
    }

    public void deleteFollowonNewsFeed(Long ownerId, Long followUserId) {
        NewsFeedDto newsFeed = createNewsFeed(null, ownerId, "FOLLOW", followUserId, null, null);
        kafkaNewsFeedProducer.sendDeleteNewsFeedMessage(newsFeed);
        log.info("Follow NewsFeed Deleted");
    }

    public void deleteCommentLikeonNewsFeed(Long ownerId, Long commentId) {
        NewsFeedDto newsFeed = createNewsFeed(null, ownerId, "COMMENTLIKE", null, null, commentId);
        kafkaNewsFeedProducer.sendDeleteNewsFeedMessage(newsFeed);
        log.info("CommentLike NewsFeed Deleted");
    }

    public void deletePostLikeonNewsFeed(Long ownerId, Long postId) {
        NewsFeedDto newsFeed = createNewsFeed(null, ownerId, "POSTLIKE", null, postId, null);
        kafkaNewsFeedProducer.sendDeleteNewsFeedMessage(newsFeed);
        log.info("PostLike NewsFeed Deleted");
    }


}
