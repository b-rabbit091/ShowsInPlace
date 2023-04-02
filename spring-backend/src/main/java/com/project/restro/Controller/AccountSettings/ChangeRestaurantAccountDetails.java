package com.project.restro.Controller.AccountSettings;

import com.project.restro.Controller.RBase_Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account_settings/restaurant")
public class ChangeRestaurantAccountDetails extends RBase_Controller {
/*
    @PutMapping("/update_contact/{id}")
    public ResponseEntity<?> restaurantUpdateContact(@PathVariable Long id, @RequestBody Map<String, String> contactMap) {
        String newContact = contactMap.get(("newContact"));
        return updateContact(id, newContact);
    }

    @PutMapping("/update_location/{id}")
    public ResponseEntity<?> restaurantUpdateLocation(@PathVariable Long id, @RequestBody Map<String, String> locationMap) {

        String location = locationMap.get("location");
        return updateLocation(id, location);
    }

    @PutMapping("/update_password/{id}")
    public ResponseEntity<?> restaurantUpdatePassword(@PathVariable("id") Long id, @RequestBody PasswordUpdateRequest request) {

        String email = request.getEmail();
        String currentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();
        return updatePassword(id, email, currentPassword, newPassword);
    }

    private ResponseEntity<?> updatePassword(Long id, String email, String currentPassword, String newPassword) {
        try {
            restaurantService.updatePassword(id, email, currentPassword, newPassword);
            return ResponseEntity.ok().body("Password updated successfully");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.status(500).body("Internal Server Error");
        }

    }

    private ResponseEntity<?> updateLocation(Long id, String location) {
        boolean updateSuccessful = restaurantService.updateLocation(id, location);

        if (updateSuccessful) {
            System.out.println("ROB" + ResponseEntity.ok().build());
            return ResponseEntity.ok().body("Location updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }


    private ResponseEntity<?> updateContact(Long id, String newContact) {

        boolean updateSuccessful = restaurantService.updateContact(id, newContact);
        if (updateSuccessful) {
            return ResponseEntity.ok().body("Contacts updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }
*/

}
