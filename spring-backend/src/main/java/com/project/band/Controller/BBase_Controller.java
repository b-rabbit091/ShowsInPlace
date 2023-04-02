package com.project.band.Controller;

import com.project.band.Response.BandProfileResponse;
import com.project.band.Service.BandService;
import com.project.restro.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BBase_Controller {

    @Autowired
   protected BandService bandService ;

    @Autowired
    protected RestaurantService restaurantService;




}
