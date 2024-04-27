package com.example.activitymodule.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //유저 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //게시글과 연관관계

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>();

    private String content;


    @Builder
    public Comment(Long userId, Post post, List<CommentLike> commentLikes, String content){
        this.userId = userId;
        this.post = post;
        this.commentLikes = commentLikes;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }

}
