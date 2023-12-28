package com.project.firstTry.repository;

import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.chamber.id_chamber = :chamberId")
    void deleteByChamberId(@Param("chamberId") long chamberId);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.users.id_user = :userId")
    void deleteByUserId(@Param("userId") long userId);
}

