package com.example.tp_spring.repository;

import com.example.tp_spring.entity.Ranking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends CrudRepository<Ranking, Integer> {
}