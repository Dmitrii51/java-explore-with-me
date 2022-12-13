package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT c FROM Comment c " +
            "WHERE c.author.id = ?1 " +
            "AND c.status != ?2")
    List<Comment> findAllByAuthorId(Integer authorId, CommentStatus status, Pageable page);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Comment c SET c.status = status " +
            "WHERE c.id = ?1")
    void updateCommentStatus(Integer commentId, CommentStatus status);

    @Query(value = "SELECT c FROM Comment c " +
            "WHERE c.event.id = ?1 " +
            "AND c.status NOT IN ?2")
    List<Comment> findEventComments(Integer eventId, List<CommentStatus> forbiddenStatusForSearch, Pageable page);

    List<Comment> findAllByAuthorId(Integer userId, Pageable page);

    List<Comment> findAllByEventId(Integer eventId, Pageable page);
}
