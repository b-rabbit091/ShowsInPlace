package com.project.restro.Entity.Restaurant;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
public class RestaurantEntity {

    @Id
    Long restaurantId;
    @Nonnull
    String restaurantName;




}
