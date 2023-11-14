package com.example.tp_spring.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String path;

    private String status;

    @ManyToMany
    @JoinTable(
            name = "image_tags",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    public Image() {
    }
    public Image(String name, String status, Set<Tag> tags) {
        this.name = name;
        this.path = "/images/" + name;
        this.status = status;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
