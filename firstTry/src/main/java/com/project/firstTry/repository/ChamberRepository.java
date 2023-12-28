package com.project.firstTry.repository;

import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChamberRepository extends JpaRepository<Chamber,Long> {
    @Query("SELECT c FROM Chamber c")
    List<Chamber> findAllChambersWithQuery();

    List<Chamber> findByCategoriesType(String type);

    @Modifying
    @Query("DELETE FROM Chamber c WHERE c.id_chamber = :chamberId")
    void deleteChamberById(@Param("chamberId") long chamberId);


}
