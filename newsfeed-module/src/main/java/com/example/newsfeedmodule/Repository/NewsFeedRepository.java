package com.example.newsfeedmodule.Repository;

import com.example.newsfeedmodule.Domain.NewsFeed;
import com.example.newsfeedmodule.Domain.NewsFeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {
    void deleteByPostId(Long postId);

    // 댓글에 해당하는 뉴스피드 삭제
    void deleteByCommentId(Long commentId);

    // 팔로워의 뉴스피드에서 언팔로우한 유저의 활동 삭제
    void deleteByOwnUserIdAndFollowUserId(Long ownUserId, Long followUserId);

    // 게시글 좋아요취소 시 해당하는 뉴스피드 삭제
    void deleteByOwnUserIdAndPostIdAndType(Long userId, Long postId, NewsFeedType type);

    // 댓글 좋아요취소 시 해당하는 뉴스피드 삭제
    void deleteByOwnUserIdAndCommentIdAndType(Long userId, Long commentId, NewsFeedType type);

    //boolean existsByUserAndPostAndType(User targetUser, Post post, NewsFeedType type);

    //boolean existsByUserAndCommentAndType(User targetUser, Comment comment, NewsFeedType type);

    Page<NewsFeed> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT nf FROM NewsFeed nf WHERE nf.userId = :userId" +
            " AND (:cursorId IS NULL OR nf.id < :cursorId)" +
            " ORDER BY nf.id DESC")
    List<NewsFeed> findNewsFeedsByUserIdBeforeCursor(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);

    //boolean existsByUserAndFollowUserAndType(User targetUser, User followUser, NewsFeedType type);

}
