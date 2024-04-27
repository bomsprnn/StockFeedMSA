package com.example.activitymodule.Repository;


import com.example.activitymodule.Domain.Post;
import com.example.activitymodule.Domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserIdAndPost(Long userId, Post post);

    int countByPost(Post post);

    PostLike findByUserIdAndPost(Long userId, Post post);

    void deleteByUserIdAndPost(Long userId, Post post);
}
