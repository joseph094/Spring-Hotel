package com.project.firstTry.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.firstTry.dto.ReservationRequest;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.service.impl.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
@CrossOrigin
    @PostMapping()
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationRequest reservationRequest) {

        Reservation createdReservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok(createdReservation);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        try {
            List<Reservation> userReservations = reservationService.getReservationsByUserId(userId);
            return new ResponseEntity<>(userReservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long reservationId) {
        Optional<Reservation> reservation = reservationService.getReservationById(reservationId);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody Reservation updatedReservation) {
        Optional<Reservation> existingReservation = reservationService.getReservationById(reservationId);
        if (existingReservation.isPresent()) {
            updatedReservation.setId_res(reservationId);
            reservationService.updateReservation(updatedReservation);
            return ResponseEntity.ok(updatedReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        Optional<Reservation> reservation = reservationService.getReservationById(reservationId);
        if (reservation.isPresent()) {
            reservationService.deleteReservation(reservationId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
