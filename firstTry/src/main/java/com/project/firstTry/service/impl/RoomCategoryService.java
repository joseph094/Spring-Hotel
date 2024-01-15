// Import necessary packages
package com.project.firstTry.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.repository.RoomCategoryRepository;

import jakarta.transaction.Transactional;

// Service annotation indicating that this class is a Spring service
@Service
public class RoomCategoryService {
    // Logger for logging messages
    private static final Logger log = LoggerFactory.getLogger(RoomCategory.class);

    // Autowired field-based dependency injection
    @Autowired
    private RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private ChamberRepository chamberRepository;

    // Method to retrieve all room categories
    public List<RoomCategory> getAllRoomCategories() {
        return roomCategoryRepository.findAll();
    }

    // Method to create or update a room category
    public RoomCategory createOrUpdateRoomCategory(RoomCategory roomCategory) {
        return roomCategoryRepository.save(roomCategory);
    }

    // Method to delete a room category by its type
    @Transactional
    public ResponseEntity<Object> deleteRoomCategory(String categoryId) {
        Optional<RoomCategory> roomCategory = roomCategoryRepository.findByType(categoryId);

        if (roomCategory.isPresent()) {
            List<Chamber> chambers = chamberRepository.findAll();

            // Iterate through chambers to remove the association with the deleted room category
            for (Chamber chamber : chambers) {
                log.info("Check chambers: {}, Mohamed: {}", chamber.getCategories(), "mohamed");

                if (chamber.getCategories() != null && chamber.getCategories().getType().equals(categoryId)) {
                    chamber.setCategories(null);
                    chamberRepository.save(chamber);
                }
            }

            // Delete the room category
            roomCategoryRepository.delete(roomCategory.get());
            return ResponseEntity.noContent().build();
        } else {
            // If room category is not present, return not found
            return ResponseEntity.notFound().build();
        }
    }

    // Method to retrieve a room category by its type
    @Transactional
    public Optional<RoomCategory> getRoomCategoryByType(String type) {
        return roomCategoryRepository.findByType(type);
    }

    // Method to update a room category by its type
    @Transactional
    public ResponseEntity<Object> updateRoomCategory(String categoryId, RoomCategory updatedRoomCategory) {
        Optional<RoomCategory> existingRoomCategory = roomCategoryRepository.findByType(categoryId);

        if (existingRoomCategory.isPresent()) {
            RoomCategory roomCategoryToUpdate = existingRoomCategory.get();

            // Update fields of the existing room category with the values from the updated room category
            roomCategoryToUpdate.setPrice_winter(updatedRoomCategory.getPrice_winter());
            roomCategoryToUpdate.setPrice_spring(updatedRoomCategory.getPrice_spring());
            roomCategoryToUpdate.setPrice_summer(updatedRoomCategory.getPrice_summer());
            roomCategoryToUpdate.setPrice_autmn(updatedRoomCategory.getPrice_autmn());
            roomCategoryToUpdate.setCaracterstiques(updatedRoomCategory.getCaracterstiques());

            // Save the updated room category
            roomCategoryRepository.save(roomCategoryToUpdate);

            return ResponseEntity.noContent().build();
        } else {
            // If room category is not present, return not found
            return ResponseEntity.notFound().build();
        }
    }
}
