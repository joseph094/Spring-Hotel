package com.project.firstTry.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Reservations")

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_res;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chamber_id")
    private Chamber chamber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "users_id")
    private Users users;

    @JsonFormat(pattern = "dd/MM/yyyy")

    private Date Begin_Date;
    @JsonFormat(pattern = "dd/MM/yyyy")

    private Date End_Date;

    private Long full_price;


}
