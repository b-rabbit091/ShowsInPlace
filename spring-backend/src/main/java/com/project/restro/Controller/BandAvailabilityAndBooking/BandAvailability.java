package com.project.restro.Controller.BandAvailabilityAndBooking;

import com.project.band.Entity.Band.BandEntity;
import com.project.band.Response.BandProfileResponse;
import com.project.restro.Controller.RBase_Controller;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class BandAvailability extends RBase_Controller {
    @GetMapping("/band_availability")
    public List<BandProfileResponse> available_bands_on_given_date(@RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate) {
        return restaurantService.findBandByIdWithoutBookingsOnGivenDate(selectedDate);
    }

}
