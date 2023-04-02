package com.project.band.Controller.RegistrationAndLogin;

import com.project.band.Controller.BBase_Controller;
import com.project.band.Entity.Details.BandDetailsEntity;
import com.project.band.Entity.SocialMedia.BandSocialMediaEntity;
import com.project.band.Response.BandProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/band")
public class BandLoginController extends BBase_Controller {


    @PostMapping("/login_verification")
    public ResponseEntity<BandProfileResponse> band_login_verification(@RequestBody Map<String, String> loginData) {
        String email = (String) loginData.get("email");
        String password = (String) loginData.get("password");
        System.out.println("email " + email);
        System.out.println("password" + password);
        return prepare_band_login_response(email, password);

    }

    private ResponseEntity<BandProfileResponse> prepare_band_login_response(String email, String password) {
        try {
            Long bandId = bandService.getId(email, password);
            System.out.println("band id is "+ bandId);
            if (bandId != null) {
               BandProfileResponse bandProfileResponse = bandService.retrieveBandProfileFromBandId(bandId);
                return ResponseEntity.ok(bandProfileResponse);
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
