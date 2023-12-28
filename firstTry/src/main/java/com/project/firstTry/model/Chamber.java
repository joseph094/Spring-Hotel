package com.project.firstTry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Chambers")
public class Chamber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id_chamber;

    private int nbr_chamber;

    private int floor;

    @OneToMany(mappedBy = "chamber",fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categories_id", nullable = true)
    private RoomCategory categories;

}
