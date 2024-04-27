package com.example.activitymodule.Repository;


import com.example.activitymodule.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);
    List<Comment> findByPostIdOrderByCreatedAt(Long postId);


}
