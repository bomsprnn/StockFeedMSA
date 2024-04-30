package com.example.newsfeedmodule.Service;

import com.example.newsfeedmodule.Domain.NewsFeed;
import com.example.newsfeedmodule.Domain.NewsFeedType;
import com.example.newsfeedmodule.Dto.NewsFeedDto;
import com.example.newsfeedmodule.Repository.NewsFeedRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;
    private final NewsFeedRepository newsFeedRepository;

    @KafkaListener(topics = "create-newsfeed", groupId = "newsfeed-group")
    public void createListen(String msg) throws Exception {
        try {
            NewsFeedDto newsFeedDto = objectMapper.readValue(msg, NewsFeedDto.class);
            System.out.println("Received Message in group newsfeed: " + newsFeedDto.toString());

            // DTO를 엔티티로 변환
            NewsFeed newsFeed = convertDtoToEntity(newsFeedDto);

            // 변환 엔티티 저장
            saveNewsFeed(newsFeed);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "delete-newsfeed", groupId = "newsfeed-group")
    public void deleteListen(String msg) throws Exception {

        NewsFeedDto newsFeedDto = objectMapper.readValue(msg, NewsFeedDto.class);
        System.out.println("Received Message in group newsfeed: " + newsFeedDto.toString());

        if(newsFeedDto.getType().equals("POST")){
          newsFeedRepository.deleteByPostId(newsFeedDto.getPostId());
          log.info("NewsFeed deleted: {}", newsFeedDto.getPostId());
        } else if(newsFeedDto.getType().equals("COMMENT")){
            newsFeedRepository.deleteByCommentId(newsFeedDto.getCommentId());
            log.info("NewsFeed deleted: {}", newsFeedDto.getCommentId());
        } else if(newsFeedDto.getType().equals("FOLLOW")){
            newsFeedRepository.deleteByOwnUserIdAndFollowUserId(newsFeedDto.getOwnUserId(), newsFeedDto.getFollowUserId());
            log.info("NewsFeed deleted: {}", newsFeedDto.getFollowUserId());
        } else if(newsFeedDto.getType().equals("POSTLIKE")){
            newsFeedRepository.deleteByOwnUserIdAndPostIdAndType(newsFeedDto.getOwnUserId(), newsFeedDto.getPostId(), NewsFeedType.valueOf("POSTLIKE"));
            log.info("NewsFeed deleted: {}", newsFeedDto.getPostId());
        } else if(newsFeedDto.getType().equals("COMMENTLIKE")){
            newsFeedRepository.deleteByOwnUserIdAndCommentIdAndType(newsFeedDto.getOwnUserId(), newsFeedDto.getCommentId(), NewsFeedType.valueOf("COMMENTLIKE"));
            log.info("NewsFeed deleted: {}", newsFeedDto.getCommentId());
        } else {
            log.error("Error processing message: {}", "Invalid Type");
        }

    }

    // NewsFeedDto를 NewsFeed 엔티티로 변환하는 메소드
    private NewsFeed convertDtoToEntity(NewsFeedDto newsFeedDto) {
        return NewsFeed.builder()
                .userId(newsFeedDto.getUserId())
                .ownUserId(newsFeedDto.getOwnUserId())
                .type(newsFeedDto.getType())
                .followUserId(newsFeedDto.getFollowUserId())
                .postId(newsFeedDto.getPostId())
                .commentId(newsFeedDto.getCommentId())
                .build();
    }

    // 변환된 NewsFeed 엔티티를 저장하는 메소드
    private void saveNewsFeed(NewsFeed newsFeed) {
        newsFeedRepository.save(newsFeed);
        log.info("NewsFeed saved: {}", newsFeed);
    }
}
