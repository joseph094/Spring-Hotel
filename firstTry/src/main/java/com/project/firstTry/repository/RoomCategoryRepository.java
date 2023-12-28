package com.project.firstTry.repository;

import com.project.firstTry.model.Categories;
import com.project.firstTry.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
    Optional<RoomCategory> findByType(String type);

    void deleteByType(Categories type);



}