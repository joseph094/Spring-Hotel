package com.project.firstTry.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.service.impl.RoomCategoryService;

/**
 * Controller class for handling RoomCategory-related operations by the admin.
 */
@RestController
@RequestMapping("/api/v1/admin/room-categories")
public class RoomCategoryController {

    // Autowired RoomCategoryService for dependency injection
    @Autowired
    private RoomCategoryService roomCategoryService;

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @GetMapping
    public List<RoomCategory> getAllRoomCategories() {
        // Retrieve all room categories from the service
        return roomCategoryService.getAllRoomCategories();
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @PostMapping
    public ResponseEntity<RoomCategory> createOrUpdateRoomCategory(@RequestBody RoomCategory roomCategory) {
        // Create or update a room category using the RoomCategoryService
        RoomCategory savedCategory = roomCategoryService.createOrUpdateRoomCategory(roomCategory);
        // Return the saved room category with a 200 OK status
        return ResponseEntity.ok(savedCategory);
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @DeleteMapping("/{type}")
    public ResponseEntity<Void> deleteRoomCategory(@PathVariable String type) {
        // Delete a room category by type using the RoomCategoryService
        ResponseEntity response = roomCategoryService.deleteRoomCategory(type);
        return response;
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @GetMapping("/{type}")
    public ResponseEntity<RoomCategory> getRoomCategoryByType(@PathVariable String type) {
        // Retrieve a room category by type using the RoomCategoryService
        Optional<RoomCategory> roomCategory = roomCategoryService.getRoomCategoryByType(type);
        // Return the room category if found with a 200 OK status, or a 404 Not Found status otherwise
        return roomCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @PutMapping("/{type}")
    public ResponseEntity<Object> updateRoomCategory(
            @PathVariable String type,
            @RequestBody RoomCategory updatedRoomCategory
    ) {
        // Update a room category by type using the RoomCategoryService
        return roomCategoryService.updateRoomCategory(type, updatedRoomCategory);
    }
}
