package com.example.garden.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 3)
    @Column(unique=true, nullable=false)
    private String code;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Registration> registration= new ArrayList<>();

    public Currency() {
    }

    public Currency(String code) {
          this.code = code;
    }

    public @Size(min = 2, max = 3) String getCode() {
        return code;
    }

    public void setCode(@Size(min = 2, max = 3) String code) {
        this.code = code;
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
