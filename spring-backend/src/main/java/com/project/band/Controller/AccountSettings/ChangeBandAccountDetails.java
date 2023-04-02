package com.project.band.Controller.AccountSettings;

import com.project.band.Controller.BBase_Controller;
import com.project.restro.Request.PasswordUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account_settings/band")
public class ChangeBandAccountDetails extends BBase_Controller {

    @PutMapping("/update_contact/{id}")
    public ResponseEntity<?> bandUpdateContact(@PathVariable Long id, @RequestBody Map<String, String> contactMap) {
        String newContact = contactMap.get(("newContact"));
        return updateContact(id, newContact);
    }

    @PutMapping("/update_location/{id}")
    public ResponseEntity<?> bandUpdateLocation(@PathVariable Long id, @RequestBody Map<String, String> locationMap) {

        String location = locationMap.get("location");
        return updateLocation(id, location);
    }

    @PutMapping("/update_password/{id}")
    public ResponseEntity<?> bandUpdatePassword(@PathVariable("id") Long id, @RequestBody PasswordUpdateRequest request) {

        String email = request.getEmail();
        String currentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();
        return updatePassword(id, email, currentPassword, newPassword);
    }

    private ResponseEntity<?> updatePassword(Long id, String email, String currentPassword, String newPassword) {
        try {
            bandService.updatePassword(id, email, currentPassword, newPassword);
            return ResponseEntity.ok().body("Password updated successfully");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.status(500).body("Internal Server Error");
        }

    }

    private ResponseEntity<?> updateLocation(Long id, String location) {
        boolean updateSuccessful = bandService.updateLocation(id, location);

        if (updateSuccessful) {
            System.out.println("ROB" + ResponseEntity.ok().build());
            return ResponseEntity.ok().body("Location updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }


    private ResponseEntity<?> updateContact(Long id, String newContact) {

        boolean updateSuccessful = bandService.updateContact(id, newContact);
        if (updateSuccessful) {
            System.out.println("passowrd changes");
            return ResponseEntity.ok().body("Contacts updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }


}
