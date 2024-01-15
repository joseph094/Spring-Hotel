// Import necessary packages
package com.project.firstTry.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.firstTry.dto.ChamberRequestToReserve;
import com.project.firstTry.dto.ReservationRequest;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.repository.ReservationRepository;
import com.project.firstTry.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;

// Service annotation indicating that this class is a Spring service
@Service
public class ReservationService {
    // Logger for logging messages
    private static final Logger log = LoggerFactory.getLogger(Reservation.class);

    // Autowired constructor-based dependency injection
    private final ReservationRepository reservationRepository;
    private final UsersRepository usersRepository;
    private final ChamberRepository chamberRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UsersRepository usersRepository, ChamberRepository chamberRepository) {
        this.reservationRepository = reservationRepository;
        this.usersRepository = usersRepository;
        this.chamberRepository = chamberRepository;
    }

    // Method to retrieve all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Method to retrieve a reservation by its ID
    public Optional<Reservation> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    // Method to update a reservation
    public void updateReservation(Reservation reservation) {
        // Additional validation or business logic can be added here
        reservationRepository.save(reservation);
    }

    // Method to delete a reservation by its ID
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    // Method to create a new reservation
    public Reservation createReservation(ReservationRequest reservationRequest) {
        // Check if the chamber exists
        Chamber chamber = chamberRepository.findById(reservationRequest.getChamberId())
                .orElseThrow(() -> new EntityNotFoundException("Chamber not found with id: " + reservationRequest.getChamberId()));
        Users user = usersRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reservationRequest.getUserId()));

        // Check if there are any existing reservations for the specified chamber and dates
        if (hasCollision(chamber, reservationRequest.getBeginDate(), reservationRequest.getEndDate())) {
            throw new RuntimeException("Chamber is already reserved for the specified dates");
        }

        // Create and save the reservation
        Reservation reservation = new Reservation();
        reservation.setChamber(chamber);
        reservation.setUsers(user);
        reservation.setBegin_Date(reservationRequest.getBeginDate());
        reservation.setEnd_Date(reservationRequest.getEndDate());
        reservation.setFull_price(reservationRequest.getFull_price());

        return reservationRepository.save(reservation);
    }

    // Method to check if there is a collision between reservations for a chamber in a specified date range
    private boolean hasCollision(Chamber chamber, Date beginDate, Date endDate) {
        // Fetch existing reservations for the specified chamber
        List<Reservation> existingReservations = reservationRepository.findAll();

        // Check for collisions with the provided date range
        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getChamber() == chamber) {
                if (isOverlap(existingReservation.getBegin_Date(), existingReservation.getEnd_Date(), beginDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to check if two date ranges overlap
    private boolean isOverlap(Date start1, Date end1, Date start2, Date end2) {
        log.info("Start1: {}, End1: {}, Start2: {}, End2: {}", start1, end1, start2, end2);
        log.info("Checking overlap: {}", start2.before(end1) && end2.after(start1));
        return start2.before(end1) && end2.after(start1);
    }

    // Method to find an available chamber of a specified type and date range
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

    // Method to find the first available chamber of a specified type and date range
    public Optional<Chamber> findAvailableChambersByTypeAndDateRange(String type, ChamberRequestToReserve chamberRequestToReserve) {
        // Fetch all chambers of the specified type
        List<Chamber> chambersOfType = chamberRepository.findByCategoriesType(type);

        // Find the first chamber that has no reservations in the specified date range
        Optional<Chamber> firstAvailableChamber = chambersOfType.stream()
                .filter(chamber -> hasCollision(chamber, chamberRequestToReserve.getBeginDate(), chamberRequestToReserve.getEndDate()))
                .findFirst();

        return firstAvailableChamber;
    }

    // Method to find all available chambers for a specified date range
    public List<Chamber> findAllAvailableChambersByDateRange(ChamberRequestToReserve chamberRequestToReserve) {
        // Fetch all chambers
        List<Chamber> allChambers = chamberRepository.findAll();

        // Find all chambers that have no reservations in the specified date range
        List<Chamber> availableChambers = allChambers.stream()
                .filter(chamber -> !hasCollision(chamber, chamberRequestToReserve.getBeginDate(), chamberRequestToReserve.getEndDate()))
                .collect(Collectors.toList());

        log.debug("Checking overlap: {}", availableChambers);

        return availableChambers;
    }

    // Method to get reservations for a specified user
    public List<Reservation> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = getAllReservations();

        // Filter reservations that are associated with the specified userId
        List<Reservation> userReservations = reservations.stream()
                .filter(reservation -> reservation.getUsers().getId_user() == userId)
                .collect(Collectors.toList());

        return userReservations;
    }

    // Method to get reservations for a specified chamber
    public List<Reservation> getReservationsByChamberId(Long chamberId) {
        // Fetch all reservations
        List<Reservation> allReservations = reservationRepository.findAll();

        // Filter reservations that are associated with the specified chamberId
        List<Reservation> chamberReservations = allReservations.stream()
                .filter(reservation -> reservation.getChamber().getId_chamber() == chamberId)
                .collect(Collectors.toList());

        return chamberReservations;
    }
}
