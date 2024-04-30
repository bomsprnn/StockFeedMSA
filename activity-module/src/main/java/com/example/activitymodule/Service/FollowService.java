package com.example.activitymodule.Service;

import com.example.activitymodule.Domain.Follow;
import com.example.activitymodule.Repository.FollowRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;
    private final JWTParseService jwtParseService;
    private final NewsFeedService newsFeedService;

    // 팔로우
    public void follow(Long followeeId, HttpServletRequest request) {
        Long followerId = jwtParseService.getUserIdFromToken(request);
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        if (followRepository.findByFollowerIdAndFollowingId(followerId, followeeId).isPresent()) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = Follow.builder()
                .followerId(followerId)
                .followingId(followeeId)
                .build();
        followRepository.save(follow);
        newsFeedService.createFollowonNewsFeed(followerId, followeeId);
    }

    // 언팔로우
    public void unfollow(Long unfolloweeId, HttpServletRequest request) {
        Long unfollowerId = jwtParseService.getUserIdFromToken(request);
        if (unfollowerId.equals(unfolloweeId)) {
            throw new IllegalArgumentException("자기 자신을 언팔로우할 수 없습니다.");
        }
        if (followRepository.findByFollowerIdAndFollowingId(unfollowerId, unfolloweeId).isEmpty()) {
            throw new IllegalArgumentException("팔로우하지 않은 사용자입니다.");
        }

        followRepository.deleteByFollowerIdAndFollowingId(unfollowerId, unfolloweeId);
        newsFeedService.deleteFollowonNewsFeed(unfollowerId, unfolloweeId);
    }

    // 팔로우 여부 확인
    public boolean isFollowing(HttpServletRequest request, Long followeeId) {
        Long followerId = jwtParseService.getUserIdFromToken(request);
        return followRepository.findByFollowerIdAndFollowingId(followerId, followeeId).isPresent();
    }

    // 팔로워 수 반환
    public int countFollowers(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    // 팔로잉 수 반환
    public int countFollowings(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    // 팔로워 목록 반환
    public List<Long> getFollowers(HttpServletRequest request) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        List<Follow> followList = followRepository.findByFollowingId(userId);
        List<Long> followersId = new ArrayList<>();
        for (Follow follow : followList) {
            followersId.add(follow.getFollowerId());
        }
        return followersId;
    }

    // 팔로잉 목록 반환
    public List<Long> getFollowings(HttpServletRequest request) {
        Long userId = jwtParseService.getUserIdFromToken(request);
        List<Follow> followList = followRepository.findByFollowerId(userId);
        List<Long> followingsId = new ArrayList<>();
        for (Follow follow : followList) {
            followingsId.add(follow.getFollowingId());
        }
        return followingsId;
    }
}
