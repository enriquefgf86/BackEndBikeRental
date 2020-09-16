package com.example.bikestore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BikeType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    @OneToMany (mappedBy= "bikeType",fetch= FetchType.EAGER)
    private Set<Bike> bikeSet=new HashSet<>();

    private String type;
    private Double price;

    public BikeType(){}
    public BikeType( String type,Double price){
        this.type=type;
        this.price=price;
    }

    public void addBike(Bike bike){
        bikeSet.add(bike);
    }

    //////////////////////////////getters///////////////////////////////
    public Long getId() {        return Id;    }
    public String getType() {        return type;    }
    public Double getPrice() {        return price;    }
    public Set<Bike> getBike() {        return bikeSet;    }


    //////////////////////////setter/////////////////////////////////
    public void setId(Long id) {        Id = id;    }
    public void setType(String type) {        this.type = type;    }
    public void setPrice(Double price) {        this.price = price;    }
    public void setBike(Set<Bike> bike) {        this.bikeSet = bike;    }
}
