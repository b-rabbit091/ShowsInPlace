package com.project.restro.Controller;

/*
@RestController
@RequestMapping("/account_settings/restaurant")
public class Restaurant_Controller extends RBase_Controller {


    @GetMapping("/band_availability")
    public List<Band> available_bands_on_given_date(@RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectedDate) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(selectedDate);
        System.out.println("Formatted date is " + formattedDate);
        List<Band> availableBandsOnGivenDate = restaurantService.available_bands_on_given_date(formattedDate);
        return availableBandsOnGivenDate;
    }


    @PutMapping("/update_contact/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody Map<String, String> contactMap) {
        String newContact = contactMap.get(("newContact"));
        boolean updateSuccessful = restaurantService.updateContact(id, newContact);

        if (updateSuccessful) {
            return ResponseEntity.ok().body("Contacts updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }

    @PutMapping("/update_location/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody Map<String, String> locationMap) {

        boolean updateSuccessful = restaurantService.updateLocation(id, locationMap.get("location"));

        if (updateSuccessful) {
            System.out.println("ROB" + ResponseEntity.ok().build());
            return ResponseEntity.ok().body("Location updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update location");
        }
    }

    @PutMapping("/update_password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable("id") Long id, @RequestBody PasswordUpdateRequest request) {

        try {
            boolean updateSuccessful = restaurantService.updatePassword(id, request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
            System.out.println(updateSuccessful);
            return ResponseEntity.ok().body("Password updated successfully");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                System.out.println("in catch block");
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


}

*/
