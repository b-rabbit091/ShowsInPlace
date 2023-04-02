package com.project.restro.Controller.BandAvailabilityAndBooking;

import com.project.band.Response.BandProfileResponse;
import com.project.restro.Controller.RBase_Controller;
import com.project.restro.Request.BookingRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurant")
public class BandBooking extends RBase_Controller {
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {

        try {
            Map<String, String> response = new HashMap<>();
            System.out.println("booking request " + request);
            restaurantService.createBooking(request);
            response.put("message", "Booking created successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create booking: " + e.getMessage());
        }

    }
}
