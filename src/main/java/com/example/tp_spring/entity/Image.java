package com.example.tp_spring.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String path;

    private String status;

    @ManyToMany
    @JoinTable(
            name = "image_tags",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @ManyToOne
    private User user;

    public Image() {
    }
    public Image(String name, String status, Set<Tag> tags, User user) {
        this.name = name;
        this.path = "/images/" + name;
        this.status = status;
        this.tags = tags;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", tags=" + tags +
                ", status='" + status + '\'' +
                '}';
    }
}
