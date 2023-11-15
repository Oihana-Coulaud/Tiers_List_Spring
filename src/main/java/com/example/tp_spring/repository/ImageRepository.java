package com.example.tp_spring.repository;

import com.example.tp_spring.entity.Image;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {
    List<Image> getImagesByStatus(String status);

    @Transactional
    @Modifying
    @Query("UPDATE Image i SET i.status = :newStatus WHERE i.id = :id")
    void updateStatusById(@Param("id") Integer id, @Param("newStatus") String newStatus);

}