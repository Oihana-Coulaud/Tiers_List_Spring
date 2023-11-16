package com.example.tp_spring.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TiersList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TiersList() {
    }

    // Constructor with parameters
    public TiersList(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @OneToMany(mappedBy = "tiersList", cascade = CascadeType.ALL)
    private List<Ranking> rankings;

    public Integer getId() {
        return id;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ranking> getRanks() {
        return rankings;
    }

    public void setRanks(List<Ranking> rankings) {
        this.rankings = rankings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TiersList{");
        stringBuilder.append("id=").append(id);
        stringBuilder.append(", name='").append(name).append('\'');

        // Iterate over rankings and append relevant information
        if (rankings != null) {
            stringBuilder.append(", rankings=[");
            for (Ranking ranking : rankings) {
                stringBuilder.append("{rankingId=").append(ranking.getId()).append(", images=").append(ranking.getImages()).append("}, ");
            }
            stringBuilder.append("]");
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
