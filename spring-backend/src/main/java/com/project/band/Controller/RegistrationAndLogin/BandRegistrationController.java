package com.project.band.Controller.RegistrationAndLogin;


import com.project.band.Controller.BBase_Controller;
import com.project.band.Request.BandRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/band")
public class BandRegistrationController extends BBase_Controller {


    @PostMapping("/registration")
    public ResponseEntity<Object> register_band(@RequestBody BandRegistrationRequest payload) {
        return registration( payload);
    }



    public ResponseEntity<Object> registration( @RequestBody BandRegistrationRequest payload) {
        try {
            System.out.println("attri" + payload);

                bandService.band_registration(payload);

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
