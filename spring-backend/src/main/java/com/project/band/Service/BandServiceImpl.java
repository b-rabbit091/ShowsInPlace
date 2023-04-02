package com.project.band.Service;

import com.project.band.Entity.Band.BandEntity;
import com.project.band.Entity.Details.BandDetailsEntity;
import com.project.band.Entity.Login.BandLoginEntity;
import com.project.band.Repository.Band.BandRepository;
import com.project.band.Request.BandRegistrationRequest;
import com.project.band.Entity.SocialMedia.BandSocialMediaEntity;
import com.project.band.Repository.Band_Repository;
import com.project.band.Repository.Details.BandDetailsRepository;
import com.project.band.Repository.Login.BandLoginRepository;
import com.project.band.Repository.SocialMedia.BandSocialMediaRepository;
import com.project.band.Response.BandProfileResponse;
import com.project.exception.ResourceNotFoundException;
import com.project.restro.Entity.RBB.RestaurantBookingBandEntity;
import com.project.restro.Entity.RBB.RBPK;
import com.project.restro.Repository.RestaurantBookingBand.RestaurantBookingBandRepository;
import com.project.restro.Request.BookingRequest;
import com.project.restro.Request.BookingStatus;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BandServiceImpl implements BandService {

    @Autowired
    Band_Repository band_repository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    BandLoginEntity bandLoginEntity;


    @Autowired
    BandDetailsEntity bandDetailsEntity;

    @Autowired
    BandLoginRepository bandLoginRepository;

    @Autowired
    BandDetailsRepository bandDetailsRepository;

    @Autowired
    BandSocialMediaEntity bandSocialMediaEntity;

    @Autowired
    BandSocialMediaRepository bandSocialMediaRepository;

    @Autowired
    BandEntity band;
    @Autowired
    BandRepository bandRepository;

    @Autowired
    RestaurantBookingBandRepository restaurantBookingBandRepository;


    @Override
    public ResponseEntity<Void> band_registration(BandRegistrationRequest bandRegistration) {

        try {

            String name = bandRegistration.getBandName();
            String email = bandRegistration.getEmail();
            String id = generateBandId(name, email);
            String hashedPassword = passwordEncoder.encode(bandRegistration.getPassword());
            //details
            String basedOn = bandRegistration.getBasedOn();
            String genre = bandRegistration.getGenre();
            Long charges = bandRegistration.getCharges();
//social mediFca

            String facebookUrl = bandRegistration.getFacebookUrl();
            String youtubeUrl = bandRegistration.getYoutubeUrl();
            String instagramUrl = bandRegistration.getInstagramUrl();


            if (checkIfNameAlreadyExists(name)) {
                throw new RuntimeException("Band with same name and location already registered");
            } else if (checkIfEmailAlreadyExists(email)) {
                throw new RuntimeException("Band with same email already registered");

            }
            Long convertedId = Long.parseLong(id.substring(0, 5), 16);

            band.setBandId(convertedId);
            band.setBandName(name);
            bandRepository.save(band);

            bandLoginEntity.setBandId(convertedId);
            bandLoginEntity.setEmail(email);
            bandLoginEntity.setPassword(hashedPassword);
            bandLoginRepository.save(bandLoginEntity);

            bandDetailsEntity.setBandId(convertedId);
            bandDetailsEntity.setBasedOn(basedOn);
            bandDetailsEntity.setCharges(charges);
            bandDetailsEntity.setGenre(genre);
            bandDetailsRepository.save(bandDetailsEntity);

            bandSocialMediaEntity.setBandId(convertedId);
            bandSocialMediaEntity.setInstagramUrl(instagramUrl);
            bandSocialMediaEntity.setFacebookUrl(facebookUrl);
            bandSocialMediaEntity.setYoutubeUrl(youtubeUrl);

            bandSocialMediaRepository.save(bandSocialMediaEntity);

            System.out.println(bandLoginEntity);
            System.out.println(bandDetailsEntity);
            System.out.println(bandSocialMediaEntity);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public boolean checkIfNameAlreadyExists(@Param("bandName") String bandName) {
        List<BandEntity> checkExist = band_repository.findByBandName(bandName);
        System.out.println("Band check exist" + checkExist);
        return checkExist.size() != 0;
    }


    private boolean checkIfEmailAlreadyExists(String email) {
        List<BandLoginEntity> band_email = bandLoginRepository.findByEmail(email);
        System.out.println("band email " + band_email);
        return band_email.size() == 1;
    }

    private String generateBandId(String name, String contact) {
        String concatenatedString = name + contact;
        byte[] hashedBytes = DigestUtils.sha256(concatenatedString.getBytes());
        return Hex.encodeHexString(hashedBytes);
    }


    @Override
    public Long getId(@Param("email") String email, @Param("password") String password) {
        TypedQuery<Long> query = entityManager.createQuery(
                        "SELECT b.id FROM BandLoginEntity b WHERE b.email = :email", Long.class)
                .setParameter("email", email);
        Long restaurantId;
        try {
            restaurantId = query.getSingleResult();
        } catch (NoResultException ex) {
            throw new ResourceNotFoundException("Band", "email", email);
        } catch (NonUniqueResultException ex) {
            throw new IllegalStateException("Multiple Band found with the same email: " + email);
        }
        String hashedPasswordOptional = entityManager.createQuery(
                        "SELECT b.password  FROM BandLoginEntity b WHERE b.email = :email", String.class)
                .setParameter("email", email)
                .getSingleResult();
        if (passwordEncoder.matches(password, hashedPasswordOptional)) {
            return restaurantId;
        } else {
            throw new RuntimeException("Invalid password for email " + email);
        }
    }


    public List findRestaurantWithoutBookingsOnGivenDate(LocalDate selectedDate) {
        Query query = entityManager.createQuery("SELECT new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact,rd.managerName,rd.about)" +
                "FROM  RestaurantEntity r " +
                "JOIN RestaurantDetailsEntity rd ON r.restaurantId = rd.restaurantId " +
                "WHERE r.restaurantId NOT IN (SELECT rr.id.restaurantId FROM RestaurantBookingBandEntity rr WHERE DATE (rr.id.bookingDate) = :selectedDate)");
        query.setParameter("selectedDate", selectedDate);
        return query.getResultList();

    }


    @Override
    public String getLocation(Long bandId) {
        Optional<BandDetailsEntity> bandOptional = bandDetailsRepository.findById(bandId);
        if (bandOptional.isPresent()) {
            BandDetailsEntity bandDetailsEntity = bandOptional.get();
            return bandDetailsEntity.getBasedOn();
        } else {
            throw new ResourceNotFoundException("Band", "id", bandId);
        }
    }

    @Override
    public String getContact(Long bandId) {
        Optional<BandDetailsEntity> bandOptional = bandDetailsRepository.findById(bandId);
        if (bandOptional.isPresent()) {
            BandDetailsEntity bandDetailsEntity = bandOptional.get();
            return bandDetailsEntity.getContact();
        } else {
            throw new ResourceNotFoundException("Band", "id", bandId);
        }
    }


    @Override
    public BandDetailsEntity getBandDetailsInfo(Long bandId) {
        Optional<BandDetailsEntity> bandOptional = bandDetailsRepository.findById(bandId);
        if (bandOptional.isPresent()) {
            return bandOptional.get();
        } else {
            throw new ResourceNotFoundException("Band", "id", bandId);
        }
    }

    @Override
    public BandSocialMediaEntity getSocialMediaInfo(Long bandId) {
        Optional<BandSocialMediaEntity> bandOptional = bandSocialMediaRepository.findById(bandId);
        if (bandOptional.isPresent()) {
            return bandOptional.get();
        } else {
            throw new ResourceNotFoundException("Band", "id", bandId);
        }
    }


    public BandProfileResponse retrieveBandProfileFromBandId(Long bandId) {
        Query query = entityManager.createQuery("SELECT new com.project.band.Response.BandProfileResponse(b.bandId, b.bandName, bd.basedOn, bd.genre, bd.contact, bd.charges, bsm.facebookUrl, bsm.instagramUrl, bsm.youtubeUrl) " +
                "FROM BandEntity b " +
                "JOIN BandDetailsEntity bd ON b.bandId = bd.bandId " +
                "JOIN BandSocialMediaEntity bsm ON b.bandId = bsm.bandId " +
                "WHERE b.bandId = :bandId ");
        query.setParameter("bandId", bandId);
        System.out.println("res" + query.getResultList());
        return (BandProfileResponse) query.getSingleResult();
    }


    @Override
    public boolean updateLocation(Long id, String location) {
        BandDetailsEntity band = bandDetailsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Restaurant ", "id", id));
        if (band != null) {
            band.setBasedOn(location);
            bandDetailsRepository.save(band);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updatePassword(Long id, String restaurantEmail, String currentPassword, String newPassword) {

        BandLoginEntity restaurant = bandLoginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", id));

        // Check if the restaurant email matches
        if (!restaurant.getEmail().equals(restaurantEmail)) {
            throw new IllegalArgumentException("Invalid email for restaurant with ID " + id);
        }

        // Check if the current password matches
        if (!passwordEncoder.matches(currentPassword, restaurant.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update the password
        restaurant.setPassword(passwordEncoder.encode(newPassword));
        bandLoginRepository.save(restaurant);

        return true;
    }

    @Override
    public boolean updateContact(Long restaurantId, String newContact) {
        BandDetailsEntity band = bandDetailsRepository.findById(restaurantId).orElse(null);
        if (band != null) {
            band.setContact(newContact);
            bandDetailsRepository.save(band);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public ResponseEntity<?> acceptRequestFromRestaurant(BookingRequest request) {
        try {
            // Find the entity by its composite primary key
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }
            RestaurantBookingBandEntity entity = optionalEntity.get();

            // Update the entity based on the request
            if (request.getRequestFrom().equalsIgnoreCase("b")) {
                entity.setBandStatus(BookingStatus.ACCEPT);
                entity.setRestaurantStatus(BookingStatus.CONFIRMED);
            }

            // Save the updated entity
            entity = restaurantBookingBandRepository.save(entity);

            return ResponseEntity.ok("Accepted request successfully!");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to accept booking request: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<?> declineRequestFromRestaurant(BookingRequest request) {
        try {
            // Find the entity by its composite primary key
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking request not found");
            }
            RestaurantBookingBandEntity entity = optionalEntity.get();

            // Update the entity based on the request
            if (request.getRequestFrom().equalsIgnoreCase("b")) {
                restaurantBookingBandRepository.delete(optionalEntity.get());

            }

            // Save the updated entity
            entity = restaurantBookingBandRepository.save(entity);

            return ResponseEntity.ok("Declined request successfully!");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decline booking request: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> cancelRequestedRestaurant(BookingRequest request) {
        try {
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking request not found");
            }
            RestaurantBookingBandEntity entity = optionalEntity.get();

            if (request.getRequestFrom().equalsIgnoreCase("b")) {
                restaurantBookingBandRepository.delete(optionalEntity.get());
            }


            return ResponseEntity.ok("Declined request successfully!");
        } catch (Exception e) {
            System.out.println("err  ->"+ e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decline booking request: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> retrieveAcceptedRestaurantIdAndBookingDate(Long bandId) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT b.id.bookingDate, new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact, rd.managerName, rd.about) " +
                            "FROM RestaurantBookingBandEntity b " +
                            "JOIN RestaurantEntity r ON r.restaurantId = b.id.restaurantId " +
                            "JOIN RestaurantDetailsEntity rd ON rd.restaurantId = b.id.restaurantId " +
                            "WHERE b.id.bandId = :bandId " +
                            "AND b.bandStatus = 4 " +
                            "ORDER BY b.id.bookingDate ASC"
            );
            query.setParameter("bandId", bandId);
            List results = query.getResultList();
            if (results.size() == 0) {
                throw new RuntimeException("No accepted restaurant bookings were found for the given band ID.");
            }
            return results;
        } catch (Exception e) {
            // Handle the exception here, for example log it or re-throw a custom exception
            throw new RuntimeException("An error occurred while retrieving accepted restaurant bookings.", e);
        }
    }


    public List<Object[]> retrieveRestaurantIdAndBookingDate(Long bandId) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT b.id.bookingDate, new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact, rd.managerName, rd.about) " +
                            "FROM RestaurantBookingBandEntity b " +
                            "JOIN RestaurantEntity r ON r.restaurantId = b.id.restaurantId " +
                            "JOIN RestaurantDetailsEntity rd ON rd.restaurantId = b.id.restaurantId " +
                            "WHERE b.id.bandId = :bandId " +
                            "AND b.bandStatus =1 " +
                            "ORDER BY b.id.bookingDate ASC"
            );
            query.setParameter("bandId", bandId);
            if(query.getResultList().size()==0)
                return Collections.emptyList();
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving requests", e);
        }
    }


    public List<Object[]> retrieveRequestedRestaurantIdAndBookingDate(Long bandId) {
        try{
        Query query = entityManager.createQuery(
                "SELECT b.id.bookingDate, new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact, rd.managerName, rd.about) " +
                        "FROM RestaurantBookingBandEntity b " +
                        "JOIN RestaurantEntity r ON r.restaurantId = b.id.restaurantId " +
                        "JOIN RestaurantDetailsEntity rd ON rd.restaurantId = b.id.restaurantId " +
                        "WHERE b.id.bandId = :bandId " +
                        "AND b.bandStatus = 0 " +
                        "ORDER BY b.id.bookingDate ASC"
        );
        query.setParameter("bandId", bandId);
            if(query.getResultList().size()==0)
                return Collections.emptyList();
        return query.getResultList();}
        catch (Exception e){
            throw new RuntimeException("An error occurred while retrieving requested restaurant", e);
        }

    }


}
