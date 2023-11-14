package com.example.tp_spring.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Image> tags;

    public Tag() {
    }
    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
