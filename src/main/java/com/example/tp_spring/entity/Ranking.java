package com.example.tp_spring.entity;

import jakarta.persistence.*;

@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "tiers_list_id", referencedColumnName = "id")
    private TiersList tiersList;

    public Ranking() {
    }

    // Constructor with parameters
    public Ranking(String name, TiersList tiersList) {
        this.name = name;
        this.tiersList = tiersList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TiersList getTiersList() {
        return tiersList;
    }

    public void setTiersList(TiersList tiersList) {
        this.tiersList = tiersList;
    }
}
