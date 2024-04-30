package com.example.newsfeedmodule.Service;

import com.example.newsfeedmodule.Domain.NewsFeed;
import com.example.newsfeedmodule.Dto.NewsFeedDto;
import com.example.newsfeedmodule.Repository.NewsFeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NewsFeedService {
    private final NewsFeedRepository newsFeedRepository;

    public List<NewsFeedDto> getNewsFeedsWithCursor(Long userId, Long cursorId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        List<NewsFeed> newsFeeds = newsFeedRepository.findNewsFeedsByUserIdBeforeCursor(userId, cursorId, pageRequest);
        log.info("뉴스피드 조회 완료"+newsFeeds.size());
        return newsFeeds.stream().map(newsFeed -> new NewsFeedDto(
                newsFeed.getId(),
                newsFeed.getUserId(),
                newsFeed.getOwnUserId(),
                newsFeed.getType().toString(),
                (newsFeed.getFollowUserId() != null) ? newsFeed.getFollowUserId() : null,
                (newsFeed.getPostId() != null) ? newsFeed.getPostId() : null,
                (newsFeed.getCommentId() != null) ? newsFeed.getCommentId() : null
        )).collect(Collectors.toList());
    }

}
