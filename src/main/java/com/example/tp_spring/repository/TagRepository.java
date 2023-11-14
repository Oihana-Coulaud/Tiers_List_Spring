package com.example.tp_spring.repository;

import com.example.tp_spring.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByName(String name);

    @Query("SELECT t.name FROM Tag t ORDER BY t.name ASC")
    List<String> findAllTagNamesOrderedAlphabetically();
}