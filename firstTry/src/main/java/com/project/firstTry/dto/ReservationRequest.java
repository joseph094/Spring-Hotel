package com.project.firstTry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReservationRequest {
    private Long chamberId;
    private Long userId;
    @JsonFormat(pattern = "dd/MM/yyyy")

    private Date beginDate;
    @JsonFormat(pattern = "dd/MM/yyyy")

    private Date endDate;

    private long full_price;
}

