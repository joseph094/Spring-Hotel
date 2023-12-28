package com.project.firstTry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_categories")
public class RoomCategory {


    @Id

    @Column(name = "type")
    private String type;

    @Column
    private BigDecimal price_winter;

    @Column
    private BigDecimal price_spring;

    @Column
    private BigDecimal price_summer;

    @Column
    private BigDecimal price_autmn;

    @Lob
    @Column(name="caracterstiques", length=512)
    private String caracterstiques;
    @JsonIgnore

    @OneToMany(mappedBy = "categories",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Chamber> chambers;

    // Getters Aand setters
}