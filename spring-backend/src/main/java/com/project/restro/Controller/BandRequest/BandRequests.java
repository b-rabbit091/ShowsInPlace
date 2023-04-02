package com.project.restro.Controller.BandRequest;


import com.project.band.Controller.BBase_Controller;
import com.project.restro.Request.BookingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurant")
public class BandRequests extends BBase_Controller {


    @GetMapping("/band_requests")
    public ResponseEntity<List> requestsFromBands(@RequestParam("restaurantId") Long restaurantId) {
        try {
            List result = restaurantService.retrieveBandIdAndBookingDate(restaurantId);

            if (result.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/band_requested")
    public ResponseEntity<List> requestedFromBands(@RequestParam("restaurantId") Long restaurantId) {
        try {
            List result = restaurantService.retrieveRequestedBandIdAndBookingDate(restaurantId);
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    //already accept bhaisakeko bands haru return
    @GetMapping("/band_accepted")
    public ResponseEntity<List> acceptedBands(@RequestParam("restaurantId") Long restaurantId) {
        try {
            List result = restaurantService.retrieveAcceptedBandIdAndBookingDate(restaurantId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorMessage));
        }
    }


    //request ayeko band ko accept
    @PutMapping("/bookings_accept")
    public ResponseEntity<?> acceptRequestFromBand(@RequestBody BookingRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
            restaurantService.acceptRequestFromBand(request);
            response.put("message", "Booking accepted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to accept booking: " + e.getMessage());
        }
    }

    @PutMapping("/bookings_decline")
    public ResponseEntity<?> declineRequestFromBand(@RequestBody BookingRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
            restaurantService.declineRequestFromBand(request);
            response.put("message", "Booking declined successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to decline booking: " + e.getMessage());
        }
    }


    @PutMapping("/requested_bookings/cancel")
    public ResponseEntity<?> cancelRequestedBand(@RequestBody BookingRequest request) {
        try {
            Map<String, String> response = new HashMap<>();
                restaurantService.cancelRequestedRestaurant(request);
            response.put("message", "Booking cancelled successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to decline booking: " + e.getMessage());
        }


    }
}
