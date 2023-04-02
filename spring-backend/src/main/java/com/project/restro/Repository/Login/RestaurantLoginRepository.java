package com.project.restro.Repository.Login;

import com.project.restro.Entity.Login.RestaurantLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantLoginRepository extends JpaRepository<RestaurantLoginEntity,Long> {
    List<RestaurantLoginEntity> findByEmail(String email);
}
