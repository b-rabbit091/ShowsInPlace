package com.project.restro.Response;

import lombok.Data;

@Data
public class RestaurantProfileResponse {

    Long restaurantId;
    String restaurantName;
  private String managerName, basedOn,contact,about;

    public RestaurantProfileResponse(Long restaurantId, String restaurantName,
                                     String basedOn, String contact, String managerName, String about) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.basedOn = basedOn;
        this.contact = contact;
        this.managerName = managerName;
        this.about = about;


    }
}
