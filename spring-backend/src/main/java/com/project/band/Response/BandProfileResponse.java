package com.project.band.Response;



import jakarta.persistence.Id;
import jakarta.persistence.Query;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class BandProfileResponse  {


    Long bandId;
    String bandName;
    boolean success;
    private String basedOn, genre, contact,about;
    private Long charges;
    private String facebookUrl, instagramUrl, youtubeUrl;

    public BandProfileResponse(Long bandId, String bandName, String basedOn, String genre, String contact, Long charges,  String facebookUrl, String instagramUrl, String youtubeUrl) {
        this.bandId = bandId;
        this.bandName = bandName;
        this.basedOn = basedOn;
        this.genre = genre;
        this.contact = contact;
        this.charges = charges;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.youtubeUrl = youtubeUrl;
    }
}
