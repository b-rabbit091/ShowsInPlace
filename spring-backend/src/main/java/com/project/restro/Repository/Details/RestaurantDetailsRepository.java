package com.project.restro.Repository.Details;

import com.project.restro.Entity.Details.RestaurantDetailsEntity;
import com.project.restro.Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantDetailsRepository extends JpaRepository<RestaurantDetailsEntity,Long> {
}
