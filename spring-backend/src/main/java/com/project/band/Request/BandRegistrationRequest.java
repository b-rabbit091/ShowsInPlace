package com.project.band.Request;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class BandRegistrationRequest {
    @Id
    Long bandId;
    @Nonnull
    String bandName;
    @Nonnull
    private String password;
    @Nonnull
    private String basedOn, genre,contact;
    private Long charges;
    private int experience;
    private String facebookUrl, instagramUrl, youtubeUrl,email;

}
