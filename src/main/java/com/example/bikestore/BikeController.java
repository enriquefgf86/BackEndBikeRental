package com.example.bikestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BikeController   {
    @Autowired
    BikeRepository bikeRepository;
    @Autowired
    BikeTypeRepository bikeTypeRepository;
    @Autowired
    InventoryBikeRepository inventoryBikeRepository;
    @Autowired
    RentBikeRepository rentBikeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

/////////////////////////////all bikes response/////////////////////////////////////////////
@RequestMapping(value="/bikes/all",method= RequestMethod.GET)
    public Map<String ,Object> getAllBikesJson(Authentication authentication){
      Map<String,Object>dto=new HashMap<>();

    dto.put("bikes_inventory",bikeRepository.findAll().stream().map(bike -> makeBikeDto(bike)).collect(Collectors.toList()));

    if(authentication==null){
          dto.put("user",null);
    }
    else{
        dto.put("user",makeUserDto(securityUser(authentication)));
    }
    return dto;
}
//////////////////////////////////////registering sign user response//////////////////////////////////
    @RequestMapping(value="/bikes/user/register",method=RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>getUserRegistered(@RequestBody User user){
       if(user.getUserName().isEmpty()||user.getUserPassword().isEmpty()){
          return new ResponseEntity<>(makeResponseEntity("Error", "fill all inputs"), HttpStatus.FORBIDDEN);
       }
       if(userRepository.findByUserName(user.getUserName())!=null){
           return new ResponseEntity<>(makeResponseEntity("Error", "user exist"), HttpStatus.CONFLICT);

       }
       user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
       user.setUserRentstatus("unactive");
       User newUser=userRepository.save(user);
       return new ResponseEntity<>(makeResponseEntity("id", newUser.getId()), HttpStatus.CREATED);
    }
////////////////////////////////user detail response ///////////////////////////////////
    @RequestMapping(value="/bikes/details/for/user",method=RequestMethod.GET)
    public Map<String,Object> getUserLoggedDetailsJson(Authentication authentication){
     Map<String,Object>dto=new HashMap<>();

     User user=securityUser(authentication);
      if(user!=null){
          dto.put("user_id",user.getId());
          dto.put("user_name",user.getUserName());
          dto.put("user_rents",user.getRentBikeSet().
                  stream().sorted((r1,r2)->r1.getId().
                  compareTo(r2.getId())).map(rentBike ->
                  makeRentBikeDto(rentBike)));
      }
      else{
          dto.put("user_id","user not logged");
          dto.put("user_name","user not logged");
          dto.put("user_rents","user not logged");
      }
    return dto;
    }
    /////////////////////////////rentingBike//////////////////////////////////////////////////////
    @RequestMapping(value="/bikes/rentBike/rent/{idBike}", method=RequestMethod.PUT)
    public ResponseEntity<Map<String,Object>>devolutionBikeMethod(Authentication authentication ,@PathVariable("idBike")Long id,
                                            @RequestBody RentBike rentDetails){
        User user=securityUser(authentication);
        Bike bikeRented=bikeRepository.getOne(id);

        if(user==null){
            return new ResponseEntity<>(makeResponseEntity("Error", "Sign in or Sign up to rent bike"), HttpStatus.UNAUTHORIZED);
        }
        if(user!=null&& user.getUserRentstatus().equals("active")){
            return new ResponseEntity<>(makeResponseEntity("Error", "Devolution needed before Rent Again"), HttpStatus.CONFLICT);
        }
        RentBike rentBikeContract=new RentBike(user,bikeRented,
                rentDetails.getDaysContract(),rentDetails.getDaysContract()
                ,rentDetails.getDailyDelayFee(),rentDetails.getTotalCost(),rentDetails.getRentState());

        bikeRented.getInventoryBike().setStateRent(true);
        user.setUserRentstatus("active");
        bikeRented.getRentBikeSet().add(rentBikeContract);

        bikeRepository.save(bikeRented);
        rentBikeRepository.save(rentBikeContract);
        userRepository.save(user);

        return new ResponseEntity<>(makeResponseEntity("Success", "Bike rented"), HttpStatus.CREATED);
    }
   //////////////////////////////BikeDevolution/////////////////////////////////////////////
   @RequestMapping(value="/bikes/rentBike/devolution/{idBike}/{idRent}", method=RequestMethod.POST)
   public ResponseEntity<Map<String,Object>>devolutionBikeMethod(Authentication authentication ,@PathVariable("idBike")Long id,
                                                                 @PathVariable("idRent")Long idRent,
                                                                 @RequestBody RentBike rentDetailsDevolution){
       User user=securityUser(authentication);
       Bike bikeRentedDevolution=bikeRepository.getOne(id);
       RentBike rentId=rentBikeRepository.getOne(idRent);

       if(user==null){
           return new ResponseEntity<>(makeResponseEntity("Error", "Sign in or Sign up to rent bike"), HttpStatus.UNAUTHORIZED);
       }

       if(user!=null&& user.getUserRentstatus().equals("active")&&rentId.getUser().getUserName()!=user.getUserName()){
           return new ResponseEntity<>(makeResponseEntity("Error", "Sorry this isn't your rented bike"), HttpStatus.CONFLICT);
       }

       bikeRentedDevolution.getInventoryBike().setStateRent(false);
       user.setUserRentstatus("unactive");
       rentId.setDaysReal(rentDetailsDevolution.getDaysReal());
       rentId.setRentState(rentDetailsDevolution.getRentState());
       rentId.setDailyDelayFee((rentDetailsDevolution.getDaysReal()-rentId.getDaysContract())*3.0);
       rentId.setTotalCost(rentDetailsDevolution.getDaysReal()*rentId.getBike().getBikeType().getPrice()+
               (rentDetailsDevolution.getDaysReal()-rentId.getDaysContract())*3.0);


       bikeRepository.save(bikeRentedDevolution);
       rentBikeRepository.save(rentId);
       userRepository.save(user);
       return new ResponseEntity<>(makeResponseEntity("Success", "Bike rented was devolved"), HttpStatus.CREATED);
   }
    //================================DTO====================================================//
    /////////////////////////////////makeUserDto////////////////////////////////////
    private Map<String,Object>makeUserDto(User user){
    Map<String,Object>dto=new HashMap<>();

    dto.put("user_id",user.getId());
    dto.put("user_status",user.getUserRentstatus());
    dto.put("user_name",user.getUserName());
    dto.put("user_log_rent",user.getRentBikeSet().stream().
                            sorted((r1,r2)->r1.getId().compareTo(r2.getId())).
                            map(rentBike -> makeRentBikeDto(rentBike)));
    return dto;
    }

    /////////////////////////makeUserDtoName///////////////////////////////////
    private Map<String,Object>makeUserDtoName(User user){
        Map<String,Object>dto=new HashMap<>();

        dto.put("user_id",user.getId());
        dto.put("user_name",user.getUserName());
        return dto;
    }

    ////////////////////////////makeBikeDTO//////////////////////////////////
    private Map<String,Object>makeBikeDto(Bike bike){
       Map<String,Object>dto=new HashMap<>();

       dto.put("bike_id",bike.getId());
       dto.put("bike_type",bike.getBikeType().getType());
       dto.put("bike_description",bike.getDescription());
       dto.put("bike_img",bike.getImageUrl());
       dto.put("bike_daily_price",bike.getBikeType().getPrice());
       dto.put("bike_log_rent",bike.getRentBikeSet().stream().
                               sorted((r1,r2)->r1.getId().compareTo(r2.getId())).
                               map(rentBike -> makeRentBikeDto(rentBike)));
       dto.put("bike_log_users",bike.getUserList().stream().
                                sorted((r1,r2)->r1.getId().compareTo(r2.getId())).
                                map(user -> makeUserDtoName(user)));
       dto.put("bike_status",bike.getInventoryBike().isStateRent());
       return dto;
    }
    ///////////////////////////////////////////////makeRentBikeDto/////////////////////////////////
    private Map<String,Object>makeRentBikeDto(RentBike rentBike){
    Map<String,Object>dto=new HashMap<>();

    dto.put("rent_id",rentBike.getId());
    dto.put("rent_bike_type",rentBike.getBike().getBikeType().getType());
    dto.put("rent_bike_img",rentBike.getBike().getImageUrl());
    dto.put("rent_user",rentBike.getUser().getUserName());
    dto.put("rent_state",rentBike.getRentState());
    dto.put("rent_daily_delay_fee",rentBike.getDailyDelayFee());
    dto.put("rent_daily_price",rentBike.getBike().getBikeType().getPrice());
    dto.put("rent_days_contract",rentBike.getDaysContract());
    dto.put("rent_price_contract",rentBike.getDaysContract()*rentBike.getBike().getBikeType().getPrice());
    dto.put("rent_real_days_with_bike",rentBike.getDaysReal());
    if(rentBike.getDaysReal()>=rentBike.getDaysContract()){
        dto.put("rent_daily_delay_fee",rentBike.getDailyDelayFee());
        dto.put("rent_fee_delay_cost",(rentBike.getDaysReal()-rentBike.getDaysContract())*
                rentBike.getDailyDelayFee());
        dto.put("rent_extra_days_delay_cost",rentBike.getBike().getBikeType().getPrice()*
                (rentBike.getDaysReal()-rentBike.getDaysContract()));
        dto.put("rent_bike_total_cost",(rentBike.getBike().getBikeType().getPrice()*
                (rentBike.getDaysReal()-rentBike.getDaysContract()))+
                ((rentBike.getDaysReal()-rentBike.getDaysContract())*
                        rentBike.getDailyDelayFee())+
                (rentBike.getDaysContract()*rentBike.getBike().getBikeType().getPrice()));
    }
    else{
        dto.put("rent_daily_delay_fee",0.0);
        dto.put("rent_fee_delay_cost",0.0);
        dto.put("rent_extra_days_delay_cost",0.0);
        dto.put("rent_bike_total_cost",rentBike.getDaysContract()*rentBike.getBike().getBikeType().getPrice());
    }
//    dto.put("rent_fee_delay_cost",(rentBike.getDaysReal()-rentBike.getDaysContract())*
//                                   rentBike.getDailyDelayFee());
//    dto.put("rent_extra_days_delay_cost",rentBike.getBike().getBikeType().getPrice()*
//                                         (rentBike.getDaysReal()-rentBike.getDaysContract()));
//    dto.put("rent_bike_total_cost",rentBike.getTotalCost());

    return dto;
    }
    ////////////////////////////////////Authentication////////////////////
    //////////////////////////////////private method for user auth////////////////////////////////////
    private User securityUser(Authentication authentication){
        return userRepository.findByUserName(authentication.getName());
    }

    private Map<String,Object>makeResponseEntity(String key,Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }




}
