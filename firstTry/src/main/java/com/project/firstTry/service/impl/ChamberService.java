package com.project.firstTry.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.firstTry.dto.UpdateChamberRequest;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.repository.ReservationRepository;
import com.project.firstTry.repository.RoomCategoryRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ChamberService {

    private static final Logger log = LoggerFactory.getLogger(RoomCategory.class);

        private final ChamberRepository chamberRepository;
        private final RoomCategoryRepository roomCategoryRepository;

        private final ReservationRepository reservationRepository;


        @Autowired
        public ChamberService(ChamberRepository chamberRepository, RoomCategoryRepository roomCategoryRepository, ReservationRepository reservationRepository) {
            this.chamberRepository = chamberRepository;
            this.roomCategoryRepository = roomCategoryRepository;
            this.reservationRepository = reservationRepository;
        }

        // Create
        public Chamber createChamber(UpdateChamberRequest chamberRequest) {
            Chamber chamber = new Chamber();
            Optional<RoomCategory> roomCategoryOptional = roomCategoryRepository.findByType(chamberRequest.getCategories());
            chamber.setNbr_chamber(chamberRequest.getNbr_chamber());
            chamber.setFloor(chamberRequest.getFloor());
            roomCategoryOptional.ifPresent(chamber::setCategories);
            return chamberRepository.save(chamber);
        }

        // Read
        public Chamber getChamberById(long chamberId) {
            return chamberRepository.findById(chamberId).orElse(null);
        }
    @Transactional

        public List<Chamber> getAllChambers() {
            return chamberRepository.findAllChambersWithQuery();
        }

        // Update
        public Chamber updateChamber(long chamberId, UpdateChamberRequest updatedChamber) {
            Chamber existingChamber = chamberRepository.findById(chamberId).orElse(null);
            Optional<RoomCategory> roomCategoryOptional = roomCategoryRepository.findByType(updatedChamber.getCategories());
            if (existingChamber != null&& roomCategoryOptional.isPresent()) {
                // Update fields based on your requirements
                existingChamber.setNbr_chamber(updatedChamber.getNbr_chamber());
                existingChamber.setFloor(updatedChamber.getFloor());
                RoomCategory roomCategory = roomCategoryOptional.get();
                existingChamber.setCategories(roomCategory);
                return chamberRepository.save(existingChamber);
            }
            return null;
        }

        // Delete
        @Transactional
        public void deleteChamber(long chamberId) {
            // Delete reservations for the specified chamber directly in the database
            reservationRepository.deleteByChamberId(chamberId);

            // Delete the chamber
            chamberRepository.deleteChamberById(chamberId);
        }


        // Example: Assign Chamber to RoomCategory
        public Chamber assignChamberToRoomCategory(long chamberId, String categoryId) {
            Chamber chamber = chamberRepository.findById(chamberId).orElse(null);
                RoomCategory roomCategory = roomCategoryRepository.findById(categoryId).orElse(null);

            if (chamber != null && roomCategory != null) {
                chamber.setCategories(roomCategory);
                return chamberRepository.save(chamber);
            }

            return null;
        }

    public List<String> getAllChamberTypes() {
        List<RoomCategory> allCategories =  roomCategoryRepository.findAll();

        // Use Java Stream to extract unique types
        List<String> uniqueTypes = allCategories.stream()
                .map(category -> category.getType())
                .distinct()
                .collect(Collectors.toList());

        return uniqueTypes;
    }
    }

