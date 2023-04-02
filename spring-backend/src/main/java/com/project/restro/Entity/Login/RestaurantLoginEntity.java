package com.project.restro.Entity.Login;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
public class RestaurantLoginEntity {

    @Id
    private Long restaurantId;
    @Nonnull
    private String email;
    @Nonnull
    private String password;

}
