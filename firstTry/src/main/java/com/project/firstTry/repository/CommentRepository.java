package com.project.firstTry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.firstTry.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
//all basic crud
    
}
