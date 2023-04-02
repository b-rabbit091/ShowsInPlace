package com.project.band.Entity.SocialMedia;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
public class BandSocialMediaEntity {

    @Id
    private Long bandId;
    private String facebookUrl, instagramUrl, youtubeUrl;

}
