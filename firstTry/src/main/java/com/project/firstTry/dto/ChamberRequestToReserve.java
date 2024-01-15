// Import necessary packages
package com.project.firstTry.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

// Lombok annotation to automatically generate getters, setters, toString, and other boilerplate code
@Data
public class ChamberRequestToReserve {

    // Specify that the 'beginDate' property should be formatted as per the provided pattern when serialized to JSON
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date beginDate;

    // Specify that the 'endDate' property should be formatted as per the provided pattern when serialized to JSON
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
}
