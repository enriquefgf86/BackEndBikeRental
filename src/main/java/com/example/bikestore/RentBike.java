package com.example.bikestore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class RentBike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bike_id")
    private Bike bike;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    private Integer daysContract;
    private Integer daysReal;
    private Double dailyDelayFee=0.0;
    private Double totalCost;
    private String rentState;

    public RentBike(){}
    public RentBike(User user,Bike bike, Integer daysContract,Integer daysReal,Double dailyDelayFee,Double totalCost,String rentState){
        this.bike=bike;
        this.user=user;
        this.daysContract=daysContract;
        this.daysReal=daysReal;
        this.dailyDelayFee=dailyDelayFee;
//        if(daysReal<=daysContract){
//        totalCost=daysContract*bike.getBikeType().getPrice();
//        }
//        else{
//            totalCost=daysContract*bike.getBikeType().getPrice()+
//                    (daysReal-daysContract)*bike.getBikeType().getPrice()+(daysReal-daysContract)*3;
//
//    }
        this.totalCost=totalCost;
        this.rentState=rentState;
        user.addRent(this);
        bike.addRent(this);
    }

     /////////////////////////////getter///////////////////////////////////////////////////////
    public Long getId() {        return Id;    }
    public Bike getBike() {        return bike;    }
    public User getUser() {        return user;    }
    public Integer getDaysContract() {        return daysContract;    }
    public Integer getDaysReal() {        return daysReal;    }
    public Double getDailyDelayFee() {        return dailyDelayFee;    }
    public Double getTotalCost() {        return totalCost;    }
    public String getRentState() {        return rentState;    }
    //////////////////////////////////////////setter///////////////////////////////////////////
    public void setId(Long id) {        Id = id;    }
    public void setBike(Bike bike) {        this.bike = bike;    }
    public void setUser(User user) {        this.user = user;    }
    public void setDaysContract(Integer daysContract) {        this.daysContract = daysContract;    }
    public void setDaysReal(Integer daysReal) {        this.daysReal = daysReal;   }
    public void setDailyDelayFee(Double dailyDelayFee) {        this.dailyDelayFee = dailyDelayFee;    }
    public void setTotalCost(Double totalCost) {        this.totalCost = totalCost;    }
    public void setRentState(String rentState) {        this.rentState = rentState;    }
}
