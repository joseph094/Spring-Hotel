// Import necessary packages
package com.project.firstTry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.firstTry.model.Chamber;

// Repository annotation indicating that this interface is a Spring Data repository
@Repository
public interface ChamberRepository extends JpaRepository<Chamber, Long> {

    // Custom query to find all chambers using JPQL
    @Query("SELECT c FROM Chamber c")
    List<Chamber> findAllChambersWithQuery();

    // Method to find chambers by room category type
    List<Chamber> findByCategoriesType(String type);

    // Modifying query to delete a chamber by its ID
    @Modifying
    @Query("DELETE FROM Chamber c WHERE c.id_chamber = :chamberId")
    void deleteChamberById(@Param("chamberId") long chamberId);
}
