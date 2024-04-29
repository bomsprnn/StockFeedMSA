package com.example.newsfeedmodule.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Entity
@Getter
public class NewsFeed extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "newsfeed_id")
    private Long id;


    private Long userId; //뉴스피드 주인

    private Long ownUserId;
    //컨텐츠 게시자 (게시글, 댓글 작성자), 행위자

    private String type; //뉴스피드 타입

    private Long followUserId; //팔로우한 유저

    private Long postId; //게시글
    private Long commentId; //댓글

    @Builder
    public NewsFeed(Long userId, Long ownUserId, String type, Long followUserId, Long postId, Long commentId) {
        this.userId = userId;
        this.ownUserId = ownUserId;
        this.type = type;
        this.followUserId = followUserId;
        this.postId = postId;
        this.commentId = commentId;
    }



}
