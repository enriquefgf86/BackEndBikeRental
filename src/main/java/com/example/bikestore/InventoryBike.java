package com.example.bikestore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class InventoryBike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    @OneToOne
    @JoinTable(name="bikes",
            joinColumns = @JoinColumn(name = "inventoryBike_id"),
            inverseJoinColumns = @JoinColumn(name = "bike_id"))
    Bike bike;

    private boolean stateRent;

    public InventoryBike(){}
    public InventoryBike(Bike bike,boolean stateRent){
        this.bike=bike;
        this.stateRent=stateRent;
        bike.addInventory(this);
    }

    //////////////////////////getters///////////////////////////////
    public Long getId() {        return Id;    }
    public boolean isStateRent() {        return stateRent;    }
    public Bike getBike() { return bike;  }

    //////////////////////////////setter/////////////////////////
    public void setId(Long id) {        Id = id;    }
    public void setStateRent(boolean stateRent) {        this.stateRent = stateRent;    }
    public void setBike(Bike bike) {this.bike = bike; }
}
