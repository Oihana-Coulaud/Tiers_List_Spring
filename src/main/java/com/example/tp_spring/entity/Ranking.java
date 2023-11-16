package com.example.tp_spring.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Column(name = "`index`")
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "tiers_list_id", referencedColumnName = "id")
    private TiersList tiersList;

    @ManyToMany(mappedBy = "rankings")
    private Set<Image> images;

    public Ranking() {
    }

    // Constructor with parameters


    public Ranking(String name, Integer index, TiersList tiersList) {
        this.name = name;
        this.index = index;
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

    public Integer getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public Set<Image> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", index='" + index + '\'' +
                ", tiersList=" + tiersList +
                ", images=" + images +
                '}';
    }
}
