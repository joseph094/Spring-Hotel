// Import necessary packages
package com.project.firstTry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.firstTry.model.Categories;
import com.project.firstTry.model.RoomCategory;

// Repository interface for accessing RoomCategory entities
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, String> {

    // Method to find a room category by its type
    Optional<RoomCategory> findByType(String type);

    // Method to delete a room category by its type
    void deleteByType(Categories type);
}
