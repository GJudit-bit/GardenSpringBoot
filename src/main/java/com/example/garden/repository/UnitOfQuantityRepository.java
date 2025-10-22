package com.example.garden.repository;

import com.example.garden.model.UnitOfQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnitOfQuantityRepository extends JpaRepository<UnitOfQuantity, Integer> {

}
