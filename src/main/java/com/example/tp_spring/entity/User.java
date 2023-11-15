package com.example.tp_spring.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String email;

    private String username;

    private String password;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TiersList> tiersLists;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec hachage du mot de passe
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = hashPassword(password);
    }
    public Integer getId() {
        return id;
    }

    // Getter et Setter pour le champ Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter et Setter pour le champ Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter et Setter pour le champ Password
    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }


   public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<TiersList> getTiersLists() {
        return tiersLists;
    }

    public void setTiersLists(List<TiersList> tiersLists) {
        this.tiersLists = tiersLists;
    }
    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role=" +  role  +
                '}';
    }


}
