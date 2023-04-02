package com.project.restro.Request;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestaurantLoginResponse {
    private boolean success;
    private Long id;
    private String location;
    private String contact;


}

