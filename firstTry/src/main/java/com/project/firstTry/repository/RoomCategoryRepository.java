package com.project.firstTry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.firstTry.model.Categories;
import com.project.firstTry.model.RoomCategory;

public interface RoomCategoryRepository extends JpaRepository<RoomCategory, String> {
    Optional<RoomCategory> findByType(String type);

    void deleteByType(Categories type);



}