package com.example.garden.model;

import jakarta.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class UnitOfQuantity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2)
    @Column(unique=true, nullable=false)
    private String name;


    public UnitOfQuantity() {
    }

    public UnitOfQuantity( String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
