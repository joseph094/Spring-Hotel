// Import necessary packages
package com.project.firstTry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.firstTry.model.Reservation;

// Repository annotation indicating that this interface is a Spring Data repository
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Modifying query to delete reservations associated with a specific chamber
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.chamber.id_chamber = :chamberId")
    void deleteByChamberId(@Param("chamberId") long chamberId);

    // Modifying query to delete reservations associated with a specific user
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.users.id_user = :userId")
    void deleteByUserId(@Param("userId") long userId);
}
