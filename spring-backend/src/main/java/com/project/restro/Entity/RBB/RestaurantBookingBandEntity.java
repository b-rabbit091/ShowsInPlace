package com.project.restro.Entity.RBB;

import com.project.restro.Request.BookingStatus;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBookingBandEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "restaurantId", column = @Column(name = "RESTAURANTID")),
            @AttributeOverride(name = "bandId", column = @Column(name = "BANDID")),
            @AttributeOverride(name = "bookingDate", column = @Column(name = "BOOKINGDATE"))

    })
    public RBPK RBPK;
    @Column(name="RESTAURANTSTATUS")
    public BookingStatus restaurantStatus;
    @Column(name = "BANDSTATUS")
    public BookingStatus bandStatus;
    @Column(name = "REQUESTFROM")
    public String requestFrom;

    public int likes=0,dislikes=0;

}
