package com.project.band.Controller.RestaurantRequests;


import com.project.band.Controller.BBase_Controller;
import com.project.restro.Request.BookingRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/band")
public class RestaurantRequests extends BBase_Controller {

    @GetMapping("/restaurant_requests")
    public ResponseEntity<List> requestsFromRestaurants(@RequestParam("bandId") Long bandId) {
        try {
            List result = bandService.retrieveRestaurantIdAndBookingDate(bandId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorMessage));
        }
    }



    @GetMapping("/restaurant_requested")
    public ResponseEntity<List> requestedFromRestaurants(@RequestParam("bandId") Long bandId) {
        try {
            List<Object[]> result = bandService.retrieveRequestedRestaurantIdAndBookingDate(bandId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorMessage));
        }
    }

    //already accept bhaisakeko shows haru return
    @GetMapping("/restaurant_accepted")
    public ResponseEntity<List> acceptedRestaurants(@RequestParam("bandId") Long bandId) {
        try {
            System.out.println("this is band id in accepted" + bandId);
            List<Object[]> result = bandService.retrieveAcceptedRestaurantIdAndBookingDate(bandId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorMessage));
        }
    }


            //request ayeko restaurant ko accept
    @PutMapping("/bookings_accept")
    public ResponseEntity<?> acceptRequestFromRestaurant(@RequestBody BookingRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
            System.out.println("Booking accepted: " + request);
            bandService.acceptRequestFromRestaurant(request);
            response.put("message", "Booking accepted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to accept booking: " + e.getMessage());
        }
    }

    @PutMapping("/bookings_decline")
    public ResponseEntity<?> declineRequestFromRestaurant(@RequestBody BookingRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
            System.out.println("Booking accepted: " + request);
            bandService.declineRequestFromRestaurant(request);
            response.put("message", "Booking declined successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to decline booking: " + e.getMessage());
        }}

        @PutMapping("/requested_bookings/cancel")
        public ResponseEntity<?> cancelRequestedRestaurant(@RequestBody BookingRequest request) {
            try {
                Map<String, String> response = new HashMap<>();
                System.out.println("Booking cancleed: " + request);
                bandService.cancelRequestedRestaurant(request);
                response.put("message", "Booking cancelled successfully!");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to decline booking: " + e.getMessage());
            }



    }


}
