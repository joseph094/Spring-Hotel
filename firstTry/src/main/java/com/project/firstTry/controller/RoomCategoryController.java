package com.project.firstTry.controller;

import com.project.firstTry.model.Categories;
import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.service.impl.RoomCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/room-categories")
public class RoomCategoryController {

    @Autowired
    private RoomCategoryService roomCategoryService;
    @CrossOrigin
    @GetMapping
    public List<RoomCategory> getAllRoomCategories() {
        return roomCategoryService.getAllRoomCategories();
    }
    @CrossOrigin

    @PostMapping
    public ResponseEntity<RoomCategory> createOrUpdateRoomCategory(@RequestBody RoomCategory roomCategory) {
        RoomCategory savedCategory = roomCategoryService.createOrUpdateRoomCategory(roomCategory);
        return ResponseEntity.ok(savedCategory);
    }
    @CrossOrigin

    @DeleteMapping("/{type}")
    public ResponseEntity<Void> deleteRoomCategory(@PathVariable String type) {
        ResponseEntity response =roomCategoryService.deleteRoomCategory(type);
        return response;
    }
    @CrossOrigin

    @GetMapping("/{type}")
    public ResponseEntity<RoomCategory> getRoomCategoryByType(@PathVariable String type) {
        Optional<RoomCategory> roomCategory = roomCategoryService.getRoomCategoryByType(type);
        return roomCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin

    @PutMapping("/{type}")
    public ResponseEntity<Object> updateRoomCategory(
            @PathVariable String type,
            @RequestBody RoomCategory updatedRoomCategory
    ) {
        return roomCategoryService.updateRoomCategory(type, updatedRoomCategory);
    }
}
