package com.example.tp_spring.repository;

import com.example.tp_spring.entity.Image;

import com.example.tp_spring.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {
    List<Image> getImagesByStatus(String status);
}