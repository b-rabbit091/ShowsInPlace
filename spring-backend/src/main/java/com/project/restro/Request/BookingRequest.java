package com.project.restro.Request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;


@Data
public class BookingRequest {
    private Long bandId;
    private Long restaurantId;
    private LocalDate bookingDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String requestFrom;


}
