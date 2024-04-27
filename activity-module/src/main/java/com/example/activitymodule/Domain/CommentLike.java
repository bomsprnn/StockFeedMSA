package com.example.activitymodule.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CommentLike extends BaseEntity{ //댓글 좋아요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //유저 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment; //댓글과 연관관계

    @Builder
    public CommentLike(Long userId, Comment comment){
        this.userId = userId;
        this.comment = comment;
    }


}
