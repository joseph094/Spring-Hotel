package com.project.firstTry.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.firstTry.dto.UpdateCommentRequest;
import com.project.firstTry.model.Comment;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.CommentRepository;
import com.project.firstTry.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CommentService {

    private final UsersRepository usersRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UsersRepository usersRepository) {
        this.commentRepository = commentRepository;
        this.usersRepository = usersRepository;
    }

    // Create
    public Comment createComment(UpdateCommentRequest commentRequest, Users user) {
        Comment comment = new Comment();
        comment.setRate(commentRequest.getRate());
        comment.setContent(commentRequest.getContent());
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    // Read
    public Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Transactional
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}