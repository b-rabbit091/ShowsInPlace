package com.project.restro.Repository;

import com.project.restro.Entity.Details.RestaurantDetailsEntity;
import com.project.restro.Entity.Login.RestaurantLoginEntity;
import com.project.restro.Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity,Long> {





    @Query("SELECT r.restaurantId FROM RestaurantLoginEntity r WHERE r.email = :email AND r.password = :password")
    Optional<Long> findIdByEmailAndPassword(@Param("email") String email, @Param("password") String password);


    List<RestaurantEntity> findByRestaurantName(String restaurantName);
}
