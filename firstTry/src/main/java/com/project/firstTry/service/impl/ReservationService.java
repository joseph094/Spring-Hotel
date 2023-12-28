package com.project.firstTry.service.impl;

import com.project.firstTry.dto.ChamberRequestToReserve;
import com.project.firstTry.dto.ReservationRequest;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.repository.ReservationRepository;
import com.project.firstTry.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ReservationService {
    private static final Logger log = LoggerFactory.getLogger(Reservation.class);

    private final ReservationRepository reservationRepository;

    private final UsersRepository usersRepository;

    private final ChamberRepository chamberRepository;



    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UsersRepository usersRepository, ChamberRepository chamberRepository) {
        this.reservationRepository = reservationRepository;
        this.usersRepository = usersRepository;
        this.chamberRepository = chamberRepository;
    }


    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public void updateReservation(Reservation reservation) {
        // Additional validation or business logic can be added here
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
    public Reservation createReservation(ReservationRequest reservationRequest) {
        // Check if the chamber exists
        Chamber chamber = chamberRepository.findById(reservationRequest.getChamberId())
                .orElseThrow(() -> new EntityNotFoundException("Chamber not found with id: " + reservationRequest.getChamberId()));
        Users user =usersRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reservationRequest.getUserId()));

        // Check if there are any existing reservations for the specified chamber and dates
        if (hasCollision(chamber, reservationRequest.getBeginDate(), reservationRequest.getEndDate())) {
            throw new RuntimeException("Chamber is already reserved for the specified dates");
        }


        // Create and save the reservation
        Reservation reservation = new Reservation();
        reservation.setChamber(chamber);
        reservation.setUsers(user); // Set the users property
        reservation.setBegin_Date(reservationRequest.getBeginDate());
        reservation.setEnd_Date(reservationRequest.getEndDate());
        reservation.setFull_price(reservationRequest.getFull_price());

        return reservationRepository.save(reservation);
    }

    private boolean hasCollision(Chamber chamber, Date beginDate, Date endDate) {
        // Fetch existing reservations for the specified chamber
        List<Reservation> existingReservations =reservationRepository.findAll();
        // Check for collisions with the provided date range
        for (Reservation existingReservation : existingReservations) {
            if(existingReservation.getChamber()==chamber){
            if (isOverlap(existingReservation.getBegin_Date(), existingReservation.getEnd_Date(), beginDate, endDate)) {
                return true;
            }
        }}
        return false;
    }

    private boolean isOverlap(Date start1, Date end1, Date start2, Date end2) {
        log.info("Start1: {}, End1: {}, Start2: {}, End2: {}", start1, end1, start2, end2);
        log.info("Checking overlap: {}", start2.before(end1) && end2.after(start1));
        return start2.before(end1) && end2.after(start1);
    }
    public Optional<Chamber> findAvailableChamberByTypeAndDateRange(String type, ChamberRequestToReserve chamberRequestToReserve) {
        // Fetch all chambers of the specified type
        List<Chamber> chambersOfType = chamberRepository.findByCategoriesType(type);

        // Filter chambers that have no reservations in the specified date range
        List<Chamber> availableChambers = chambersOfType.stream()
                .filter(chamber -> !hasCollision(chamber, chamberRequestToReserve.getBeginDate(), chamberRequestToReserve.getEndDate()))
                .collect(Collectors.toList());

        // Return the first available chamber, if any
        return availableChambers.stream().findFirst();
    }
    public Optional<Chamber> findAvailableChambersByTypeAndDateRange(String type, ChamberRequestToReserve chamberRequestToReserve) {
        // Fetch all chambers of the specified type
        List<Chamber> chambersOfType = chamberRepository.findByCategoriesType(type);

        // Find the first chamber that has no reservations in the specified date range
        Optional<Chamber> firstAvailableChamber = chambersOfType.stream()
                .filter(chamber -> hasCollision(chamber, chamberRequestToReserve.getBeginDate(), chamberRequestToReserve.getEndDate()))
                .findFirst();

        return firstAvailableChamber;
    }
    public List<Chamber> findAllAvailableChambersByDateRange(ChamberRequestToReserve chamberRequestToReserve) {
        // Fetch all chambers
        List<Chamber> allChambers = chamberRepository.findAll();

        // Find all chambers that have no reservations in the specified date range
        List<Chamber> availableChambers = allChambers.stream()
                .filter(chamber -> !hasCollision(chamber, chamberRequestToReserve.getBeginDate(), chamberRequestToReserve.getEndDate()))
                .collect(Collectors.toList());
        log.debug("Checking overlap: {}",availableChambers);

        return availableChambers;
    }
    public List<Reservation> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = getAllReservations();
        List<Reservation> userReservations = reservations.stream()
                .filter(reservation -> reservation.getUsers().getId_user()==(userId))
                .collect(Collectors.toList());

        return userReservations;


    }
        // Fetch reservations for the specified user
        public List<Reservation> getReservationsByChamberId(Long chamberId) {
            // Fetch all reservations
            List<Reservation> allReservations = reservationRepository.findAll();

            // Filter reservations that are associated with the specified chamberId
            List<Reservation> chamberReservations = allReservations.stream()
                    .filter(reservation -> reservation.getChamber().getId_chamber()==chamberId)
                    .collect(Collectors.toList());

            return chamberReservations;
        }



}
