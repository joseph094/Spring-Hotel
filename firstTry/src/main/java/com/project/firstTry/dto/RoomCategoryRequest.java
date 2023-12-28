package com.project.firstTry.dto;

import com.project.firstTry.model.Categories;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
@Data

public class RoomCategoryRequest {
    private Categories type;
    private BigDecimal price;
    private Set<String> characteristics;
}
