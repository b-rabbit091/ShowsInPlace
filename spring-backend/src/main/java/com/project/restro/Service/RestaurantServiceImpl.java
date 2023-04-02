package com.project.restro.Service;

import com.project.restro.Entity.RBB.RestaurantBookingBandEntity;
import com.project.restro.Entity.RBB.RBPK;
import com.project.band.Repository.Band_Repository;
import com.project.exception.InvalidPasswordException;
import com.project.exception.ResourceNotFoundException;
import com.project.restro.Entity.Details.RestaurantDetailsEntity;
import com.project.restro.Entity.Login.RestaurantLoginEntity;
import com.project.restro.Entity.Restaurant.RestaurantEntity;
import com.project.restro.Repository.Details.RestaurantDetailsRepository;
import com.project.restro.Repository.Login.RestaurantLoginRepository;
import com.project.restro.Repository.RestaurantBookingBand.RestaurantBookingBandRepository;
import com.project.restro.Repository.RestaurantRepository;
import com.project.restro.Request.BookingRequest;
import com.project.restro.Request.BookingStatus;
import com.project.restro.Request.RestaurantRegistrationRequest;
import com.project.restro.Response.RestaurantProfileResponse;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    Band_Repository band_repository;

    @Autowired
    RestaurantEntity restaurantEntity;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RestaurantLoginEntity restaurantLoginEntity;

    @Autowired
    RestaurantDetailsEntity restaurantDetailsEntity;

    @Autowired
    RestaurantDetailsRepository restaurantDetailsRepository;

    @Autowired
    RestaurantLoginRepository restaurantLoginRepository;


    @Autowired
    RestaurantBookingBandRepository restaurantBookingBandRepository;


    public ResponseEntity<Void> register_restaurant(RestaurantRegistrationRequest restaurantRegistrationRequest) throws RuntimeException {
        try {
            String restaurantName = restaurantRegistrationRequest.getRestaurantName();
            String contact = restaurantRegistrationRequest.getContact();
            String basedOn = restaurantRegistrationRequest.getBasedOn();
            String email = restaurantRegistrationRequest.getEmail();
            String about = restaurantRegistrationRequest.getAbout();
            String managerName = restaurantRegistrationRequest.getManagerName();
            String hashedPassword = passwordEncoder.encode(restaurantRegistrationRequest.getPassword());

            if (checkIfNameAlreadyExists(restaurantName)) {
                throw new RuntimeException("Restaurant with same name and location already registered");
            } else if (checkIfEmailExists(email)) {
                throw new RuntimeException("Restaurant with same email already registered");
            }
            String id = generateRestaurantId(restaurantName, contact);
            Long convertedId = Long.parseLong(id.substring(0, 5), 16);

            restaurantDetailsEntity.setRestaurantId(convertedId);
            restaurantDetailsEntity.setAbout(about);
            restaurantDetailsEntity.setContact(contact);
            restaurantDetailsEntity.setBasedOn(basedOn);
            restaurantDetailsEntity.setManagerName(managerName);
            restaurantDetailsRepository.save(restaurantDetailsEntity);


            restaurantLoginEntity.setRestaurantId(convertedId);
            restaurantLoginEntity.setPassword(hashedPassword);
            restaurantLoginEntity.setEmail(email);

            restaurantLoginRepository.save(restaurantLoginEntity);

            restaurantEntity.setRestaurantId(convertedId);
            restaurantEntity.setRestaurantName(restaurantName);
            restaurantRepository.save(restaurantEntity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while saving the new restaurant", e);
        }
    }

    private boolean checkIfEmailExists(String email) {
        List<RestaurantLoginEntity> restaurantLoginEntities = restaurantLoginRepository.findByEmail(email);
        return restaurantLoginEntities.size() == 1;
    }

    private String generateRestaurantId(String name, String contact) {
        String concatenatedString = name + contact;
        byte[] hashedBytes = DigestUtils.sha256(concatenatedString.getBytes());
        String hashedString = Hex.encodeHexString(hashedBytes);
        return hashedString;
    }


    public boolean checkIfNameAlreadyExists(@Param("rName") String restaurant_name) {
        List<RestaurantEntity> checkExist = restaurantRepository.findByRestaurantName(restaurant_name);
        System.out.println("Restaurants check exist" + checkExist);
        return checkExist.size() != 0;
    }


    public List findBandByIdWithoutBookingsOnGivenDate(LocalDate selectedDate) {
        Query query = entityManager.createQuery("SELECT new com.project.band.Response.BandProfileResponse(b.bandId, b.bandName, bd.basedOn, bd.genre, bd.contact, bd.charges, bsm.facebookUrl, bsm.instagramUrl, bsm.youtubeUrl) " +
                "FROM BandEntity b " +
                "JOIN BandDetailsEntity bd ON b.bandId = bd.bandId " +
                "JOIN BandSocialMediaEntity bsm ON b.bandId = bsm.bandId " +
                "WHERE b.bandId NOT IN (SELECT bb.id.bandId FROM RestaurantBookingBandEntity bb WHERE DATE (bb.id.bookingDate) = :selectedDate)");
        query.setParameter("selectedDate", selectedDate);
        System.out.println("res" + query.getResultList());
        return query.getResultList();
    }

    @Override
    public RestaurantProfileResponse retrieveRestaurantProfileFromRestaurantId(Long restaurantId) {

        try {
            Query query = entityManager.createQuery("SELECT new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact,rd.managerName,rd.about) " +
                    "FROM RestaurantEntity r " +
                    "JOIN RestaurantDetailsEntity rd ON r.restaurantId = rd.restaurantId " +
                    "WHERE r.restaurantId = :restaurantId ");
            query.setParameter("restaurantId", restaurantId);
            System.out.println("res" + query.getResultList());
            return (RestaurantProfileResponse) query.getSingleResult();
        } catch (NoResultException nre) {
            throw new RuntimeException("Restaunt not found with given id.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Long getId(@Param("email") String email, @Param("password") String password) {
        TypedQuery<Long> query = entityManager.createQuery(
                        "SELECT rle.restaurantId FROM RestaurantLoginEntity rle WHERE rle.email = :email", Long.class)
                .setParameter("email", email);
        Long restaurantId;
        try {
            restaurantId = query.getSingleResult();
        } catch (NoResultException ex) {
            throw new ResourceNotFoundException("Restaurant", "email", email);
        } catch (NonUniqueResultException ex) {
            throw new IllegalStateException("Multiple restaurants found with the same email: " + email);
        }
        String hashedPasswordOptional = entityManager.createQuery(
                        "SELECT rle.password  FROM RestaurantLoginEntity rle WHERE rle.email = :email", String.class)
                .setParameter("email", email)
                .getSingleResult();
        if (passwordEncoder.matches(password, hashedPasswordOptional)) {
            return restaurantId;
        } else {
            throw new InvalidPasswordException("Invalid password for email " + email);
        }
    }

    /*

        @Override
        public String getLocation(Long restaurantId) {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
            if (restaurantOptional.isPresent()) {
                Restaurant restaurant = restaurantOptional.get();
                return restaurant.getLocation();
            } else {
                throw new ResourceNotFoundException("Restaurant", "id", restaurantId);
            }
        }

        @Override
        public String getContact(Long restaurantId) {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
            if (restaurantOptional.isPresent()) {
                Restaurant restaurant = restaurantOptional.get();
                return restaurant.getContact();
            } else {
                throw new ResourceNotFoundException("Restaurant", "id", restaurantId);
            }
        }


        @Override
        public boolean updateLocation(Long id, String location) {
            restaurant = restaurantRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException("Restaurant ", "id", id));
            if (restaurant != null) {
                restaurant.setLocation(location);
                restaurantRepository.save(restaurant);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean updatePassword(Long id, String restaurantEmail, String currentPassword, String newPassword) {

            Restaurant restaurant = restaurantRepository.findById(id)
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
            restaurantRepository.save(restaurant);

            return true;
        }
    */
    public ResponseEntity<?> createBooking(BookingRequest request) {
        try {
            RestaurantBookingBandEntity booking = new RestaurantBookingBandEntity();
            RBPK RBPK = new RBPK();
            RBPK.setBandId(request.getBandId());
            RBPK.setRestaurantId(request.getRestaurantId());
            RBPK.setBookingDate(request.getBookingDate());
            booking.setRBPK(RBPK);


            if (request.getRequestFrom().equalsIgnoreCase("r")) {
                booking.setBandStatus(BookingStatus.PENDING);
                booking.setRestaurantStatus(BookingStatus.REQUESTED);
            } else if (request.getRequestFrom().equalsIgnoreCase("b")) {
                booking.setRestaurantStatus(BookingStatus.PENDING);
                booking.setBandStatus(BookingStatus.REQUESTED);
            }

            restaurantBookingBandRepository.save(booking);
            System.out.println("in service class" + booking);

            return ResponseEntity.ok("Booking created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create booking: " + e.getMessage());
        }
    }

    public List<Object[]> retrieveRestaurantIdAndBookingDate(Long bandId) {
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
        System.out.println("request" + query.getResultList().get(0));

        return query.getResultList();
    }

//   (Long bandId, String bandName, String basedOn,
//   String genre, String contact, Long charges,
//   String facebookUrl, String instagramUrl, String youtubeUrl) {

    @Override
    public List<Object[]> retrieveBandIdAndBookingDate(Long restaurantId) {
        try {
            System.out.println("here in retrieve" + restaurantId);
            Query query = entityManager.createQuery(
                    "SELECT b.id.bookingDate, new com.project.band.Response.BandProfileResponse" +
                            "(r.bandId, r.bandName, rd.basedOn, rd.genre,rd.contact ,rd.charges" +
                            ",rds.facebookUrl, rds.instagramUrl, rds.youtubeUrl) " +
                            "FROM RestaurantBookingBandEntity b " +
                            "JOIN BandEntity r ON r.bandId = b.id.bandId " +
                            "JOIN BandDetailsEntity rd ON rd.bandId = b.id.bandId " +
                            "JOIN BandSocialMediaEntity rds ON rds.bandId = b.id.bandId " +
                            "WHERE b.id.restaurantId = :restaurantId " +
                            "AND b.restaurantStatus =1 " +
                            "ORDER BY b.id.bookingDate ASC"
            );
            query.setParameter("restaurantId", restaurantId);
            System.out.println("request" + query.getResultList().get(0));
            List results = query.getResultList();
            if (results.size() == 0) {
                throw new RuntimeException("No accepted restaurant bookings were found for the given band ID.");
            }
            return query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving band requests", e);
        }

    }

    @Override
    public List<Object[]> retrieveRequestedBandIdAndBookingDate(Long restaurantId) {
        try {
            System.out.println("here in retrieve requested" + restaurantId);
            Query query = entityManager.createQuery(
                    "SELECT b.id.bookingDate, new com.project.band.Response.BandProfileResponse" +
                            "(r.bandId, r.bandName, rd.basedOn, rd.genre,rd.contact ,rd.charges" +
                            ",rds.facebookUrl, rds.instagramUrl, rds.youtubeUrl) " +
                            "FROM RestaurantBookingBandEntity b " +
                            "JOIN BandEntity r ON r.bandId = b.id.bandId " +
                            "JOIN BandDetailsEntity rd ON rd.bandId = b.id.bandId " +
                            "JOIN BandSocialMediaEntity rds ON rds.bandId = b.id.bandId " +
                            "WHERE b.id.restaurantId = :restaurantId " +
                            "AND b.restaurantStatus =0 " +
                            "ORDER BY b.id.bookingDate ASC"
            );
            query.setParameter("restaurantId", restaurantId);
            List<Object[]> results = query.getResultList();

            System.out.println("retrieved results are "+results);
            if (results.size() == 0) {
                return Collections.emptyList();            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving requested bands", e);
        }
    }

    @Override
    public List<Object[]> retrieveAcceptedBandIdAndBookingDate(Long restaurantId) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT b.id.bookingDate, new com.project.band.Response.BandProfileResponse" +
                            "(r.bandId, r.bandName, rd.basedOn, rd.genre,rd.contact ,rd.charges" +
                            ",rds.facebookUrl, rds.instagramUrl, rds.youtubeUrl) " +
                            "FROM RestaurantBookingBandEntity b " +
                            "JOIN BandEntity r ON r.bandId = b.id.bandId " +
                            "JOIN BandDetailsEntity rd ON rd.bandId = b.id.bandId " +
                            "JOIN BandSocialMediaEntity rds ON rds.bandId = b.id.bandId " +
                            "WHERE b.id.restaurantId = :restaurantId " +
                            "AND b.restaurantStatus =3 " +
                            "ORDER BY b.id.bookingDate ASC"
            );
            query.setParameter("restaurantId", restaurantId);
            System.out.println("request" + query.getResultList().get(0));

            List results = query.getResultList();

            if (results.size() == 0) {
                return Collections.emptyList();            }
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving accepted bands", e);
        }
    }

    @Override
    public ResponseEntity<?> acceptRequestFromBand(BookingRequest request) {
        try {
            // Find the entity by its composite primary key
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }
            RestaurantBookingBandEntity entity = optionalEntity.get();

            // Update the entity based on the request
            if (request.getRequestFrom().equalsIgnoreCase("r")) {
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
    public ResponseEntity<?> declineRequestFromBand(BookingRequest request) {

        try {
            // Find the entity by its composite primary key
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking request not found");
            }
            RestaurantBookingBandEntity entity = optionalEntity.get();

            // Update the entity based on the request
            if (request.getRequestFrom().equalsIgnoreCase("r")) {
                restaurantBookingBandRepository.delete(optionalEntity.get());

            }

            // Save the updated entity

            return ResponseEntity.ok("Declined request successfully!");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decline booking request: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> cancelRequestedRestaurant(BookingRequest request) {
        try {
            Optional<RestaurantBookingBandEntity> optionalEntity = restaurantBookingBandRepository.findById(new RBPK(request.getBandId(), request.getRestaurantId(), request.getBookingDate()));
            if (!optionalEntity.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking request not found");
            }


            RestaurantBookingBandEntity entity = optionalEntity.get();

            // Update the entity based on the request
            if (request.getRequestFrom().equalsIgnoreCase("r")) {
                restaurantBookingBandRepository.delete(optionalEntity.get());
            }
            return ResponseEntity.ok("Declined request successfully!");
        } catch (Exception e) {
            System.out.println("err  ->" + e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decline booking request: " + e.getMessage());
        }
    }


}
