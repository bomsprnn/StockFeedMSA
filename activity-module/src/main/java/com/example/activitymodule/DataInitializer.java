package com.example.activitymodule;


import com.example.activitymodule.Domain.Comment;
import com.example.activitymodule.Domain.Follow;
import com.example.activitymodule.Domain.Post;
import com.example.activitymodule.Repository.CommentRepository;
import com.example.activitymodule.Repository.FollowRepository;
import com.example.activitymodule.Repository.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() {
        Post post = Post.builder()
                .title("title1")
                .content("content1")
                .userId(1L)
                .viewCount(0)
                .build();
        Post post2 = Post.builder()
                .title("title2")
                .content("content2")
                .userId(2L)
                .viewCount(0)
                .build();
        postRepository.save(post);
        postRepository.save(post2);

        Comment comment = Comment.builder()
                .content("comment1")
                .userId(1L)
                .build();
        Comment comment2 = Comment.builder()
                .content("comment2")
                .userId(2L)
                .build();
        commentRepository.save(comment);
        commentRepository.save(comment2);

        Follow follow = Follow.builder()
                .followerId(1L)
                .followingId(2L)
                .build();
        Follow follow2 = Follow.builder()
                .followerId(2L)
                .followingId(1L)
                .build();
        Follow follow3 = Follow.builder()
                .followerId(3L)
                .followingId(2L)
                .build();
        followRepository.save(follow);
        followRepository.save(follow2);
        followRepository.save(follow3);

    }
}