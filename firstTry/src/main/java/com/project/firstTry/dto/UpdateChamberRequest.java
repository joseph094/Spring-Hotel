package com.project.firstTry.dto;

import lombok.Data;

@Data
public class UpdateChamberRequest {

    private int nbr_chamber;

    private int floor;

    private String categories ;
}
