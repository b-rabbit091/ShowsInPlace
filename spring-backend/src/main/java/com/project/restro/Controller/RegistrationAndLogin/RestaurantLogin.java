package com.project.restro.Controller.RegistrationAndLogin;

import com.project.band.Response.BandProfileResponse;
import com.project.restro.Controller.RBase_Controller;
import com.project.restro.Request.RestaurantLoginResponse;
import com.project.restro.Response.RestaurantProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/restaurant")
public class RestaurantLogin extends RBase_Controller {


    @PostMapping("/login_verification")
    public ResponseEntity<RestaurantProfileResponse> restaurant_login_verification(@RequestBody Map<String, String> loginData) {
        String email = (String) loginData.get("email");
        String password = (String) loginData.get("password");
        System.out.println("here"+email+"---"+password);
        return prepare_restaurant_login_response(email, password);

    }


    public ResponseEntity<RestaurantProfileResponse> prepare_restaurant_login_response(String email, String password) {
        try {
            Long restaurantId = restaurantService.getId(email, password);
            System.out.println("restaurant id " + restaurantId);
            if (restaurantId != null) {
                RestaurantProfileResponse restaurantProfileResponse = restaurantService.retrieveRestaurantProfileFromRestaurantId(restaurantId);
                return ResponseEntity.ok(restaurantProfileResponse);

            } else throw new RuntimeException("Error in validating email and password");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "An error occurred while registering.";
            }
            System.out.println(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "An unexpected error occurred while registering.";
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

    }

}
