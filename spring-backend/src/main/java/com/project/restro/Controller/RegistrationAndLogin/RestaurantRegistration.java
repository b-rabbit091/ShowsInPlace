package com.project.restro.Controller.RegistrationAndLogin;


import com.project.restro.Controller.RBase_Controller;
import com.project.restro.Request.RestaurantRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantRegistration extends RBase_Controller {

    @PostMapping("/registration")
    public ResponseEntity<Object> register_restaurant(@RequestBody RestaurantRegistrationRequest payload) {
        return registration( payload);
    }


    public ResponseEntity<Object> registration(RestaurantRegistrationRequest payload) {
        try {
            System.out.println("attri" + payload);

            restaurantService.register_restaurant(payload);

            return ResponseEntity.status(HttpStatus.CREATED).body("Account Successfully Created");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "An error occurred while registering.";
            }
            System.out.println(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "An unexpected error occurred while registering.";
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage);
        }
    }

}
