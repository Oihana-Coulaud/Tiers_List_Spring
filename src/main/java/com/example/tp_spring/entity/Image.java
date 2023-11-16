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

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    private User user;

    @ManyToMany
    private Set<Ranking> rankings;

    public Image() {
    }
    public Image(String name, String status, Tag tag, User user) {
        this.name = name;
        this.path = "/images/" + name;
        this.status = status;
        this.tag = tag;
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
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(Set<Ranking> rankings) {
        this.rankings = rankings;
    }

    @Override
        public String toString() {
            return "Image{" +
                    "id=" + id +
                    ", path='" + path + '\'' +
                    ", tag=" +  tag.getName()  +
                    ", status='" + status + '\'' +
                    '}';
        }
}
