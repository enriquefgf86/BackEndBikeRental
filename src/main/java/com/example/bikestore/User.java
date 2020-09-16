package com.example.bikestore;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private Bike bikeUser;

    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name="user_id")
    private List<Bike> bikeUserList=new ArrayList<>();


    @OneToMany(mappedBy="user",fetch= FetchType.EAGER)
    Set<RentBike> rentBikeSet=new HashSet<>();

    private String userName;
    private String userPassword;
    private String userEmail;
    private String userRentstatus;

    public User(){}
    public User(String userName, String userPassword, String userEmail,String userRentstatus){
        this.userEmail=userEmail;
        this.userName=userName;
        this.userPassword=userPassword;
        this.userRentstatus=userRentstatus;
//        this.bikeUserList.addUser(this);
    }

    public void addRent(RentBike rentBike){
         rentBikeSet.add(rentBike);
    }

    /////////////////////////////////getters////////////////////////////////////////
    public Long getId() {        return Id;    }
    public String getUserName() {        return userName;    }
    public String getUserPassword() {        return userPassword;    }
    public String getUserEmail() {        return userEmail;    }
    public Set<RentBike> getRentBikeSet() {        return rentBikeSet;    }
    public Bike getBikeUser() {    return bikeUser; }

    public List<Bike> getBikeUserList() {        return bikeUserList;    }
    public void setBikeUserList(List<Bike> bikeUserList) {        this.bikeUserList = bikeUserList;    }
    public String getUserRentstatus() {        return userRentstatus;    }

    /////////////////////////////setterss////////////////////////////////////////////
    public void setId(Long id) {        Id = id;    }
    public void setUserName(String userName) {        this.userName = userName;    }
    public void setUserPassword(String userPassword) {        this.userPassword = userPassword;    }
    public void setUserEmail(String userEmail) {        this.userEmail = userEmail;    }
    public void setRentBikeSet(Set<RentBike> rentBikeSet) {        this.rentBikeSet = rentBikeSet;    }
    public void setBikeUser(Bike bikeUser) {  this.bikeUser = bikeUser;}
    public void setUserRentstatus(String userRentstatus) {        this.userRentstatus = userRentstatus;    }
    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", bikeUser=" + bikeUser +
                ", rentBikeSet=" + rentBikeSet +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
