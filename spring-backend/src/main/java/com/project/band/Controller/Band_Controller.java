package com.project.band.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/band")

public class Band_Controller extends BBase_Controller {

/*

    @GetMapping("/restaurant_availability")
    public List<Restaurant> available_bands_on_given_date(@RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectedDate ) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(selectedDate);
        System.out.println("Formatted date is " + formattedDate);
        List<Restaurant> availableBandsOnGivenDate = bandService.available_restaurants_on_given_date(formattedDate);
        return availableBandsOnGivenDate;
    }

*/


}
