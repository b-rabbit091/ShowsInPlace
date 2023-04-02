package com.project.restro.Request;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RestaurantRegistrationRequest {


    @Nonnull
    String restaurantName;
    @Nonnull
    private String email;
    @Nonnull
    private String password;
    @Nonnull
    private String basedOn,contact,managerName,about;

}
