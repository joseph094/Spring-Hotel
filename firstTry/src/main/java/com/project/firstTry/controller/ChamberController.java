package com.project.firstTry.controller;

import com.project.firstTry.dto.ChamberRequestToReserve;
import com.project.firstTry.dto.UpdateChamberRequest;
import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.Reservation;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.service.UserService;
import com.project.firstTry.service.impl.ChamberService;
import com.project.firstTry.service.impl.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Tag(name = "Chamber APIs", description = "chambers Apis")
@RequestMapping("/api/v1/")
public class ChamberController {

    private final ChamberService chamberService;

    private final ReservationService reservationService;

    private final UserService usersService;


    private final ChamberRepository chamberRepository;


    @Autowired
    public ChamberController(ChamberService chamberService, ReservationService reservationService, UserService userService, ChamberRepository chamberRepository) {
        this.chamberService = chamberService;
        this.reservationService = reservationService;
        this.usersService = userService;
        this.chamberRepository=chamberRepository;

    }

    @CrossOrigin
    @GetMapping("chambers/rooms")
    @Operation(
            summary = "Fetch all Chambers",
            description = "fetches all Chambers entities")
    public List<Chamber> getAllChambers() {
        return chamberService.getAllChambers();
    }
    // Create
    @CrossOrigin
    @PostMapping("admin/chambers/rooms")
    public ResponseEntity<Chamber> createChamber(@RequestBody UpdateChamberRequest chamber) {
        Chamber createdChamber = chamberService.createChamber(chamber);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChamber);
    }

    // Read
    @CrossOrigin
    @GetMapping("chambers/{chamberId}")
    public ResponseEntity<Chamber> getChamberById(@PathVariable long chamberId) {

        Chamber chamber = chamberService.getChamberById(chamberId);
        if (chamber != null) {
            return ResponseEntity.ok(chamber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @PutMapping("admin/chambers/{chamberId}")
    public ResponseEntity<Chamber> updateChamber(@PathVariable long chamberId, @RequestBody UpdateChamberRequest updatedChamber) {
        Chamber updated = chamberService.updateChamber(chamberId, updatedChamber);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
@CrossOrigin
    // Delete
    @DeleteMapping("admin/chambers/{chamberId}")
    public ResponseEntity<Void> deleteChamber(@PathVariable long chamberId) {
        chamberService.deleteChamber(chamberId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("chambers/byType/{type}")
    @CrossOrigin
    public ResponseEntity<List<Chamber>> getChambersByType(@PathVariable String type) {
        List<Chamber> chambers = chamberRepository.findByCategoriesType(type);

        if (chambers != null && !chambers.isEmpty()) {
            return ResponseEntity.ok(chambers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin
    @PostMapping("chambers/byTypeAndDates/{type}")
    public ResponseEntity<Optional<Chamber>> getChamberByTypeAndDates(
            @PathVariable String type,
            @RequestBody ChamberRequestToReserve chamberRequestToReserve
            ) {

        Optional<Chamber> chamber = reservationService.findAvailableChamberByTypeAndDateRange(type,chamberRequestToReserve);

        if (chamber != null) {
            return ResponseEntity.ok(chamber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin
    @PostMapping("chambers/available")
    public ResponseEntity<List<Chamber>> findAvailableChambers(
            @RequestBody ChamberRequestToReserve chamberRequestToReserve ) {



        List<Chamber> availableChambers = reservationService.findAllAvailableChambersByDateRange(chamberRequestToReserve);

        if (availableChambers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // No available chambers
        }

        return new ResponseEntity<>(availableChambers, HttpStatus.OK);
    }
    @CrossOrigin
    @GetMapping("chambers/types")
    public List<String> getAllChamberTypes() {
        return chamberService.getAllChamberTypes();
    }

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
    @CrossOrigin
    @GetMapping("/admin/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    @CrossOrigin
    @GetMapping("/chamber/reservations/{chamberId}")
    public List<Reservation> getReservationsByChamberId(@PathVariable Long chamberId) {
        return reservationService.getReservationsByChamberId(chamberId);
    }

    // Example: Assign Chamber to RoomCategory
   /* @PostMapping("/{chamberId}/assign/{categoryId}")
    public ResponseEntity<Chamber> assignChamberToRoomCategory(
            @PathVariable long chamberId,
            @PathVariable long categoryId) {
        Chamber assignedChamber = chamberService.assignChamberToRoomCategory(chamberId, categoryId);
        if (assignedChamber != null) {
            return ResponseEntity.ok(assignedChamber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}