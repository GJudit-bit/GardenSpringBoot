package com.example.garden.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;

    //owner of the relationship
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    @Fetch(value = FetchMode.JOIN)
    //  @JsonManagedReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "unit_of_quantity_id", nullable = true)
    @Fetch(value = FetchMode.JOIN)
    private UnitOfQuantity unitOfQuantity;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JsonBackReference
    private List<Registration> registration;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, Category category, UnitOfQuantity unitOfQuantity) {
        this.name = name;
        this.category = category;
        this.unitOfQuantity = unitOfQuantity;

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

    public UnitOfQuantity getUnitOfQuantity() {
        return unitOfQuantity;
    }

    public void setUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        this.unitOfQuantity = unitOfQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addCategory(Category category) {
        this.category = category;
        if (category != null && !category.getItems().contains(this)) {
            category.addItem(this);
        }
    }

    public List<Registration> getRegistration() {
        return registration;
    }

    public void setRegistration(List<Registration> registration) {
        this.registration = registration;
    }
}

