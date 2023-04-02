package com.project.restro.Repository.RestaurantBookingBand;

import com.project.restro.Entity.RBB.RestaurantBookingBandEntity;
import com.project.restro.Entity.RBB.RBPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantBookingBandRepository extends JpaRepository<RestaurantBookingBandEntity, RBPK> {
}
