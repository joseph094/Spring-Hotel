// Import necessary packages
package com.project.firstTry.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.project.firstTry.model.Categories;

import lombok.Data;

// Lombok annotation to automatically generate getters, setters, toString, and other boilerplate code
@Data
public class RoomCategoryResponse {

    // Private field to store the room category ID
    private Long id;

    // Private field to store the room category type
    private Categories type;

    // Private field to store the price of the room category
    private BigDecimal price;

    // Private field to store the characteristics of the room category
    private Set<String> characteristics;
}
