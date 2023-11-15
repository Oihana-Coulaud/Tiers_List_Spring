package com.example.tp_spring.repository;

import com.example.tp_spring.entity.TiersList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiersListRepository extends CrudRepository<TiersList, Integer> {
    TiersList findByName(String name);

    @Query("SELECT tl FROM TiersList tl WHERE tl.user.username = :userName")
    List<TiersList> findByUserName(@Param("userName") String userName);
}