package com.project.viewer.Service;


import com.project.band.Response.BandProfileResponse;
import com.project.restro.Entity.RBB.RBPK;
import com.project.restro.Entity.RBB.RestaurantBookingBandEntity;
import com.project.restro.Response.RestaurantProfileResponse;
import com.project.webSocket.PerformanceWebSocketHandler;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class ViewerServiceImpl implements ViewerService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PerformanceWebSocketHandler webSocketHandler;


    public BandProfileResponse getBandDetails(Long bandId) {
        try {
            TypedQuery<BandProfileResponse> query = entityManager.createQuery(
                    "SELECT new com.project.band.Response.BandProfileResponse(r.bandId, r.bandName, rd.basedOn, rd.genre, rd.contact, rd.charges, rds.facebookUrl, rds.instagramUrl, rds.youtubeUrl) " +
                            "FROM BandEntity r " +
                            "JOIN BandDetailsEntity rd ON rd.bandId = r.bandId " +
                            "JOIN BandSocialMediaEntity rds ON rds.bandId = r.bandId " +
                            "WHERE r.bandId = :bandId",
                    BandProfileResponse.class
            );
            query.setParameter("bandId", bandId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving band details for band ID " + bandId, e);
        }
    }

    public RestaurantProfileResponse getRestaurantDetails(Long restaurantId) {
        try {
            TypedQuery<RestaurantProfileResponse> query = entityManager.createQuery(
                    "SELECT new com.project.restro.Response.RestaurantProfileResponse(r.restaurantId, r.restaurantName, rd.basedOn, rd.contact, rd.managerName, rd.about) " +
                            "FROM RestaurantEntity r " +
                            "JOIN RestaurantDetailsEntity rd ON rd.restaurantId = r.restaurantId " +
                            "WHERE r.restaurantId = :restaurantId",
                    RestaurantProfileResponse.class
            );
            query.setParameter("restaurantId", restaurantId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving restaurant details for restaurant ID " + restaurantId, e);
        }
    }

    public List<Map<String, Object>> getAllPerformancesToday() {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            Query query = entityManager.createQuery(
                    "SELECT b " +
                            "FROM RestaurantBookingBandEntity b " +
                            "WHERE ((b.bandStatus = 4 ) OR ( b.restaurantStatus = 4)) " +
                            "AND b.id.bookingDate = CURRENT_DATE() " +
                            "ORDER BY b.id.bookingDate ASC"
            );

            List<RestaurantBookingBandEntity> todaysBookings = query.getResultList();
            for (RestaurantBookingBandEntity booking : todaysBookings) {
                RBPK rbpk = booking.getRBPK();
                Long bandId = rbpk.getBandId();
                Long restaurantId = rbpk.getRestaurantId();
                BandProfileResponse bandDetails = getBandDetails(bandId);
                RestaurantProfileResponse restaurantDetails = getRestaurantDetails(restaurantId);
                Map<String, Object> result = new HashMap<>();
                result.put("id", rbpk);
                result.put("bandDetails", bandDetails);
                result.put("restaurantDetails", restaurantDetails);
                result.put("likes", booking.getLikes());
                result.put("dislikes", booking.getDislikes());
                results.add(result);
            }

            if (results.size() == 0)
                return Collections.emptyList();
            return results;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving today's performances", e);
        }
    }

    @Transactional
    public ResponseEntity<String> updateLikes(RBPK rbpk) {
        try {
            RestaurantBookingBandEntity performance = entityManager.find(RestaurantBookingBandEntity.class, rbpk);
            if (performance != null) {
                performance.setLikes(performance.getLikes() + 1);
                entityManager.merge(performance);
                webSocketHandler.notifyClients("Update message");
                return ResponseEntity.ok().body("{\"message\": \"Liked\", \"likes\": " + performance.getLikes() + "}");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Performance not found with the specified RBPK.\"}");
            }
        } catch (Exception ex) {
            System.out.println("exception" + ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
    }


    @Transactional
    public ResponseEntity<String> updateDislike(RBPK rbpk) {
         try {
        RestaurantBookingBandEntity performance = entityManager.find(RestaurantBookingBandEntity.class, rbpk);
        if (performance != null) {
            performance.setDislikes(performance.getDislikes() + 1);
            entityManager.merge(performance);
            webSocketHandler.notifyClients("Update message");
            return ResponseEntity.ok().body("{\"message\": \"Disliked\", \"dislikes\": " + performance.getDislikes() + "}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Performance not found with the specified RBPK.\"}");
        }
    } catch (Exception ex) {
        System.out.println("exception" + ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"" + ex.getMessage() + "\"}");
    }
}


}
