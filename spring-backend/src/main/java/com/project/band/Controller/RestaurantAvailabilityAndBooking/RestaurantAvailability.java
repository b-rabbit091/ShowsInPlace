package com.project.band.Controller.RestaurantAvailabilityAndBooking;

import com.project.band.Controller.BBase_Controller;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/band")
public class RestaurantAvailability extends BBase_Controller {

    @GetMapping("/restaurant_availability")
    public List available_bands_on_given_date(@RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate) {
        return bandService.findRestaurantWithoutBookingsOnGivenDate(selectedDate);
    }


}
