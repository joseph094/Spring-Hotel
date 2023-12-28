package com.project.firstTry.service.impl;

import com.project.firstTry.model.Categories;
import com.project.firstTry.model.Chamber;
import com.project.firstTry.model.RoomCategory;
import com.project.firstTry.repository.ChamberRepository;
import com.project.firstTry.repository.RoomCategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomCategoryService {
    private static final Logger log = LoggerFactory.getLogger(RoomCategory.class);

    @Autowired
    private RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private ChamberRepository chamberRepository;
    public List<RoomCategory> getAllRoomCategories() {
        return roomCategoryRepository.findAll();
    }
    public RoomCategory createOrUpdateRoomCategory(RoomCategory roomCategory) {

        return roomCategoryRepository.save(roomCategory);
    }
    @Transactional

    public ResponseEntity<Object> deleteRoomCategory(String categoryId) {
        Optional<RoomCategory> roomCategory = roomCategoryRepository.findByType(categoryId);

        if (roomCategory.isPresent()) {
            List<Chamber> chambers =  chamberRepository.findAll();
            String mohamed ="mohamed";
            for(Chamber chamber : chambers) {
                log.info("Check chambers: {}, Mohamed: {}", chamber.getCategories(), mohamed);

                if(chamber.getCategories()!=null&&chamber.getCategories().getType().equals(categoryId)){
                chamber.setCategories(null);
                    chamberRepository.save(chamber);}

            }


            roomCategoryRepository.delete(roomCategory.get());
            return ResponseEntity.noContent().build();
        } else {
            // If room is not present, you can throw an exception or handle the situation accordingly
            return ResponseEntity.notFound().build(); // or throw new SomeException();
        }
    }
    @Transactional

    public Optional<RoomCategory> getRoomCategoryByType(String type) {
        return roomCategoryRepository.findByType(type);
    }

    @Transactional
    public ResponseEntity<Object> updateRoomCategory(String categoryId, RoomCategory updatedRoomCategory) {
        Optional<RoomCategory> existingRoomCategory = roomCategoryRepository.findByType(categoryId);

        if (existingRoomCategory.isPresent()) {
            RoomCategory roomCategoryToUpdate = existingRoomCategory.get();

            // Update fields of the existing roomCategory with the values from the updatedRoomCategory
            roomCategoryToUpdate.setPrice_winter(updatedRoomCategory.getPrice_winter());
            roomCategoryToUpdate.setPrice_spring(updatedRoomCategory.getPrice_spring());
            roomCategoryToUpdate.setPrice_summer(updatedRoomCategory.getPrice_summer());
            roomCategoryToUpdate.setPrice_autmn(updatedRoomCategory.getPrice_autmn());
            roomCategoryToUpdate.setCaracterstiques(updatedRoomCategory.getCaracterstiques());

            // Save the updated roomCategory
            roomCategoryRepository.save(roomCategoryToUpdate);

            return ResponseEntity.noContent().build();
        } else {
            // If roomCategory is not present, you can throw an exception or handle the situation accordingly
            return ResponseEntity.notFound().build(); // or throw new SomeException();
        }
    }
}
