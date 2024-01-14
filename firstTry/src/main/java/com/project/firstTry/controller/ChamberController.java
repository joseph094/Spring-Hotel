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

import com.project.firstTry.dto.ChamberRequestToReserve;
import com.project.firstTry.dto.UpdateChamberRequest;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.service.UserService;
import com.project.firstTry.service.impl.ChamberService;
import com.project.firstTry.service.impl.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "Chamber APIs", description = "Chambers APIs")
@RequestMapping("/api/v1/")
public class ChamberController {

    private final ChamberService chamberService;
    private final ReservationService reservationService;
    private final UserService usersService;
    private final ChamberRepository chamberRepository;

    @Autowired
    public ChamberController(ChamberService chamberService, ReservationService reservationService,
                             UserService userService, ChamberRepository chamberRepository) {
        this.chamberService = chamberService;
        this.reservationService = reservationService;
        this.usersService = userService;
        this.chamberRepository = chamberRepository;
    }

    // Fetch all Chambers
    @CrossOrigin
    @GetMapping("chambers/rooms")
    @Operation(
            summary = "Fetch all Chambers",
            description = "Fetches all Chambers entities"
    )
    public List<Chamber> getAllChambers() {
        return chamberService.getAllChambers();
    }

    // Create a new Chamber
    @CrossOrigin
    @PostMapping("admin/chambers/rooms")
    public ResponseEntity<Chamber> createChamber(@RequestBody UpdateChamberRequest chamber) {
        Chamber createdChamber = chamberService.createChamber(chamber);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChamber);
    }

    // Read a Chamber by ID
    @CrossOrigin
    @GetMapping("chambers/{chamberId}")
    public ResponseEntity<Chamber> getChamberById(@PathVariable long chamberId) {
        Chamber chamber = chamberService.getChamberById(chamberId);
        return chamber != null ? ResponseEntity.ok(chamber) : ResponseEntity.notFound().build();
    }

    // Update a Chamber by ID
    @CrossOrigin
    @PutMapping("admin/chambers/{chamberId}")
    public ResponseEntity<Chamber> updateChamber(@PathVariable long chamberId,
                                                 @RequestBody UpdateChamberRequest updatedChamber) {
        Chamber updated = chamberService.updateChamber(chamberId, updatedChamber);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Delete a Chamber by ID
    @CrossOrigin
    @DeleteMapping("admin/chambers/{chamberId}")
    public ResponseEntity<Void> deleteChamber(@PathVariable long chamberId) {
        chamberService.deleteChamber(chamberId);
        return ResponseEntity.noContent().build();
    }

    // Fetch Chambers by type
    @CrossOrigin
    @GetMapping("chambers/byType/{type}")
    public ResponseEntity<List<Chamber>> getChambersByType(@PathVariable String type) {
        List<Chamber> chambers = chamberRepository.findByCategoriesType(type);
        return chambers != null && !chambers.isEmpty() ?
                ResponseEntity.ok(chambers) :
                ResponseEntity.notFound().build();
    }

    // Fetch available Chamber by type and date range
    @CrossOrigin
    @PostMapping("chambers/byTypeAndDates/{type}")
    public ResponseEntity<Optional<Chamber>> getChamberByTypeAndDates(
            @PathVariable String type,
            @RequestBody ChamberRequestToReserve chamberRequestToReserve
    ) {
        Optional<Chamber> chamber = reservationService.findAvailableChamberByTypeAndDateRange(type, chamberRequestToReserve);
        return chamber != null ? ResponseEntity.ok(chamber) : ResponseEntity.notFound().build();
    }

    // Fetch all available Chambers by date range
    @CrossOrigin
    @PostMapping("chambers/available")
    public ResponseEntity<List<Chamber>> findAvailableChambers(
            @RequestBody ChamberRequestToReserve chamberRequestToReserve) {
        List<Chamber> availableChambers = reservationService.findAllAvailableChambersByDateRange(chamberRequestToReserve);
        return availableChambers.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(availableChambers, HttpStatus.OK);
    }

    // Fetch all Chamber types
    @CrossOrigin
    @GetMapping("chambers/types")
    public List<String> getAllChamberTypes() {
        return chamberService.getAllChamberTypes();
    }

    // Fetch user reservations by user ID
    @CrossOrigin
    @GetMapping("/user/reservations/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        try {
            List<Reservation> userReservations = reservationService.getReservationsByUserId(userId);
            return new ResponseEntity<>(userReservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Fetch all reservations (admin)
    @CrossOrigin
    @GetMapping("/admin/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // Fetch reservations by Chamber ID
    @CrossOrigin
    @GetMapping("/chamber/reservations/{chamberId}")
    public List<Reservation> getReservationsByChamberId(@PathVariable Long chamberId) {
        return reservationService.getReservationsByChamberId(chamberId);
    }
}
