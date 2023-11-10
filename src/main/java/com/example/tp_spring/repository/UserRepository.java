package com.example.tp_spring.repository;

import com.example.tp_spring.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User findByUsernameOrEmail(String username, String email);
    User findById(int id);

}