package com.example.garden.model;

import jakarta.persistence.*;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 3)
    @Column(unique=true, nullable=false)
    private String name;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.MERGE)
    private List<Registration> registration= new ArrayList<>();

    public Currency() {
    }

    public Currency(String code) {
          this.name = code;
    }

    public @Size(min = 2, max = 3) String getName() {
        return name;
    }

    public void setName(@Size(min = 2, max = 3) String name) {
        this.name = name;
    }

    public List<Registration> getRegistration() {
        return registration;
    }

    public void setRegistration(List<Registration> registration) {
        this.registration = registration;
    }

    public void addRegistration(Registration registration) {
        this.registration.add(registration);
        registration.setCurrency(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
