package com.example.activitymodule.Repository;

import com.example.activitymodule.Domain.Comment;
import com.example.activitymodule.Domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserIdAndComment(Long userId, Comment comment);

    int countByComment(Comment comment);
}
