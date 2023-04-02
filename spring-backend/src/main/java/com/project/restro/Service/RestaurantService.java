package com.project.restro.Service;

import com.project.band.Response.BandProfileResponse;
import com.project.restro.Request.BookingRequest;
import com.project.restro.Request.RestaurantRegistrationRequest;
import com.project.restro.Response.RestaurantProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Component
public interface RestaurantService {
    public ResponseEntity<Void> register_restaurant(RestaurantRegistrationRequest restaurantRegistrationRequest);
/*

    @Query("select * from Band where booking_date >= CURDATE()")
    List band_available_on_given_date(Date booking_date);

    boolean check_valid_credentials(String email, String password);
       List<BandEntity> available_bands_on_given_date(LocalDate dateInFormat);
           boolean updateContact(Long restaurantId, String newContact);


*/


    List<BandProfileResponse> findBandByIdWithoutBookingsOnGivenDate(LocalDate date);


    Long getId(String email, String password);

    RestaurantProfileResponse retrieveRestaurantProfileFromRestaurantId(Long restaurantId);

    ResponseEntity<?> createBooking(BookingRequest request);

    List <Object[]>  retrieveBandIdAndBookingDate(Long restaurantId);

    List<Object[]>  retrieveRequestedBandIdAndBookingDate(Long restaurantId);

    List<Object[]> retrieveAcceptedBandIdAndBookingDate(Long restaurantId);

    ResponseEntity<?> acceptRequestFromBand(BookingRequest request);

    ResponseEntity<?> declineRequestFromBand(BookingRequest request);

    ResponseEntity<?> cancelRequestedRestaurant(BookingRequest request);
/*
    String getLocation(Long restaurantId);

    String getContact(Long restaurantId);

    boolean updateLocation(Long id, String location);

    boolean updatePassword(Long id, String restaurantEmail, String currentPassword, String newPassword);
*/
}
