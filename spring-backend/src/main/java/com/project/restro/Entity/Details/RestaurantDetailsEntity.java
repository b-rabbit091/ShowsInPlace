package com.project.restro.Entity.Details;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity
public class RestaurantDetailsEntity {

    @Id
    private Long restaurantId;

    @Nonnull
    String basedOn, contact, managerName, about;
}
