package com.project.firstTry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.dto.UpdateCommentRequest;
import com.project.firstTry.model.Comment;
import com.project.firstTry.model.Users;
import com.project.firstTry.service.impl.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling Comment-related operations.
 */
@RestController
@CrossOrigin
@Tag(name = "Comment APIs", description = "Comments APIs")
@RequestMapping("/api")
public class CommentController {

    // Autowired CommentService for dependency injection
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Fetch all Comments.
     *
     * @return List of Comment entities.
     */
    @GetMapping("/comments")
    @Operation(
            summary = "Fetch all Comments",
            description = "Fetches all Comments entities"
    )
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    /**
     * Create a new Comment.
     *
     * @param commentRequest The request body containing data for creating a new Comment.
     * @return ResponseEntity with the created Comment or an error status.
     */
    @PostMapping("/comments")
    @Operation(
            summary = "Create a new Comment",
            description = "Creates a new Comment entity"
    )
    public ResponseEntity<Comment> createComment(@RequestBody UpdateCommentRequest commentRequest) {
        // Get the current user from the security context
        Users currentUser = getCurrentUser();
        // Create a new comment using the CommentService
        Comment createdComment = commentService.createComment(commentRequest, currentUser);

        // Check if the comment creation was successful
        if (createdComment != null) {
            // Return a 201 Created status with the created comment
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } else {
            // Return a 500 Internal Server Error if comment creation fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper method to get the current user from the security context.
     *
     * @return The current user or null if not found.
     */
    private Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users) {
            return (Users) principal;
        }
        return null;
    }

    /**
     * Read a Comment by ID.
     *
     * @param commentId The ID of the Comment to retrieve.
     * @return ResponseEntity with the retrieved Comment or a 404 Not Found status.
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long commentId) {
        // Retrieve a comment by its ID using the CommentService
        Comment comment = commentService.getCommentById(commentId);
        // Return the comment if found, or a 404 Not Found status otherwise
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }
}
