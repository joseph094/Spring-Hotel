package com.project.firstTry.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.dto.ReservationRequest;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.service.impl.ReservationService;

/**
 * Controller class for handling Reservation-related operations.
 */
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Enable Cross-Origin Resource Sharing (CORS)
    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationRequest reservationRequest) {

        // Create a new reservation using the ReservationService
        Reservation createdReservation = reservationService.createReservation(reservationRequest);
        // Return the created reservation with a 200 OK status
        return ResponseEntity.ok(createdReservation);
    }

    // Get reservations by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        try {
            // Retrieve reservations for a specific user
            List<Reservation> userReservations = reservationService.getReservationsByUserId(userId);
            // Return the user's reservations with a 200 OK status
            return new ResponseEntity<>(userReservations, HttpStatus.OK);
        } catch (Exception e) {
            // Return a 500 Internal Server Error if an exception occurs
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a reservation by ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long reservationId) {
        // Retrieve a reservation by its ID using the ReservationService
        Optional<Reservation> reservation = reservationService.getReservationById(reservationId);
        // Return the reservation if found with a 200 OK status, or a 404 Not Found status otherwise
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a reservation
    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody Reservation updatedReservation) {
        // Check if the existing reservation exists
        Optional<Reservation> existingReservation = reservationService.getReservationById(reservationId);
        if (existingReservation.isPresent()) {
            // Set the ID for the updated reservation
            updatedReservation.setId_res(reservationId);
            // Update the reservation using the ReservationService
            reservationService.updateReservation(updatedReservation);
            // Return the updated reservation with a 200 OK status
            return ResponseEntity.ok(updatedReservation);
        } else {
            // Return a 404 Not Found status if the reservation to update is not found
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a reservation by ID
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        // Check if the reservation exists
        Optional<Reservation> reservation = reservationService.getReservationById(reservationId);
        if (reservation.isPresent()) {
            // Delete the reservation using the ReservationService
            reservationService.deleteReservation(reservationId);
            // Return a 204 No Content status indicating successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return a 404 Not Found status if the reservation to delete is not found
            return ResponseEntity.notFound().build();
        }
    }
}
