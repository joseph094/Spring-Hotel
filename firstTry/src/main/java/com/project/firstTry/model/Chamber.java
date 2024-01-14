package com.project.firstTry.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
