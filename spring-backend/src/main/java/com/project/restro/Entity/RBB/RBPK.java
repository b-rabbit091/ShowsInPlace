package com.project.restro.Entity.RBB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import  jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RBPK implements Serializable {

    private Long bandId;
    private Long restaurantId;
    private LocalDate bookingDate;

    @Column(name = "BANDID")
    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    @Column(name = "RESTAURANTID")
    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Column(name = "BOOKINGDATE")
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}
