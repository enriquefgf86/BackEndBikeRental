package com.example.bikestore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    @OneToMany(mappedBy= "bike",fetch= FetchType.EAGER)
    private Set<RentBike> rentBikeSet=new HashSet<>();

//    @OneToMany(mappedBy= "bikeUser",fetch= FetchType.EAGER)
//    private List<User> userList=new ArrayList<>();

    @ManyToMany
            @JoinTable(
                    name="user_bike",
                    joinColumns = @JoinColumn(name="bike_id"),
                    inverseJoinColumns = @JoinColumn(name="user_id")
            )
    private List<User> userList=new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bike_type_id")
    private BikeType bikeType;

    @OneToOne(mappedBy = "bike")
    private InventoryBike inventoryBike;

    private String imageUrl;
    private String description;


    public Bike(){}
    public Bike(BikeType bikeType,List<User>userList,String imageUrl,String description ){
        this.bikeType=bikeType;
        this.imageUrl=imageUrl;
        this.userList=userList;
        this.description=description;

        bikeType.addBike(this);
    }

    public void addRent(RentBike rentBike){
        rentBikeSet.add(rentBike);
    }
    public void addUser(User user){
        userList.add(user);
    }

    public void addInventory(InventoryBike inventoryBikeadd){
        inventoryBike=inventoryBikeadd;
    }
    //////////////////getters///////////////////////////////
    public Long getId() {        return Id;    }
    public Set<RentBike> getRentBikeSet() {        return rentBikeSet;    }
    public List<User> getUserList() {        return userList;    }
    public BikeType getBikeType() {        return bikeType;    }
    public InventoryBike getInventoryBike() {   return inventoryBike;  }
    public String getImageUrl() {        return imageUrl;    }
    public String getDescription() {        return description;    }

    ///////////////////////////setters////////////////////////
    public void setId(Long id) {        Id = id;    }
    public void setRentBikeSet(Set<RentBike> rentBikeSet) {        this.rentBikeSet = rentBikeSet;    }
    public void setUserList(List<User> userList) {        this.userList = userList;    }
    public void setBikeType(BikeType bikeType) {        this.bikeType = bikeType;    }
    public void setInventoryBike(InventoryBike inventoryBike) { this.inventoryBike = inventoryBike;    }
    public void setImageUrl(String imageUrl) {        this.imageUrl = imageUrl;    }
    public void setDescription(String description) {        this.description = description;    }
}
