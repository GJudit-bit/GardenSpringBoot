package com.example.garden.repository;

import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UnitOfQuantityRepository extends JpaRepository<UnitOfQuantity, Integer> {

}
