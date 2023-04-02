package com.project.band.Service;

import com.project.band.Entity.Details.BandDetailsEntity;
import com.project.band.Request.BandRegistrationRequest;
import com.project.band.Entity.SocialMedia.BandSocialMediaEntity;
import com.project.band.Response.BandProfileResponse;
import com.project.restro.Request.BookingRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BandService {

    public ResponseEntity<Void>  band_registration(BandRegistrationRequest payload);

    Long getId(@Param("email") String email, @Param("password") String password);
    public BandProfileResponse retrieveBandProfileFromBandId(Long bandId) ;
    ResponseEntity<?> acceptRequestFromRestaurant(BookingRequest request);
    ResponseEntity<?> declineRequestFromRestaurant(BookingRequest request);
    String getLocation(Long bandId);

    String getContact(Long bandId);
    List findRestaurantWithoutBookingsOnGivenDate(LocalDate selectedDate);

    BandDetailsEntity getBandDetailsInfo(Long bandId);
    BandSocialMediaEntity getSocialMediaInfo(Long bandId);

    List retrieveRestaurantIdAndBookingDate(Long bandId);
    List retrieveRequestedRestaurantIdAndBookingDate(Long bandId);

    // boolean check_valid_credentials(String email, String password);
   // List<Restaurant> available_restaurants_on_given_date(String dateInFormat);
/*
    Long getId(String email, String password);
    String getLocation(Long restaurantId);
    String getContact(Long restaurantId);
*/
    boolean updateLocation(Long id, String location);

    boolean updatePassword(Long id, String restaurantEmail, String currentPassword, String newPassword);
    boolean updateContact(Long restaurantId, String newContact);

    List retrieveAcceptedRestaurantIdAndBookingDate(Long bandId);

    ResponseEntity<?>  cancelRequestedRestaurant(BookingRequest request);
}
