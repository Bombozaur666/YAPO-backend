package com.example.YAPO.repositories.plant;

import com.example.YAPO.models.plant.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    Comment findByIdAndUser_Username(Long id, String username);

    List<Comment> findAllByIdAndUser_IdAndVisible(long id, Long user_id, boolean visible);
}
