package com.project.restro.Controller;

import com.project.restro.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RBase_Controller {


    @Autowired
   protected  RestaurantService restaurantService;

  /*
    @Autowired
    protected RestaurantLoginResponse restaurantLoginResponse;

    @Autowired
    protected BandService bandService ;

    @Autowired
    protected BandLoginResponse bandLoginResponse;


     */
}
