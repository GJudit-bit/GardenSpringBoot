package com.example.garden.repository;



import com.example.garden.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

     List<Item> findByUnitOfQuantity_Id(Integer id);

    List<Item> findByCategory_Id(Integer id);
}
