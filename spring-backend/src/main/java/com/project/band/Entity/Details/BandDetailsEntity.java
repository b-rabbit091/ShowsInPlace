package com.project.band.Entity.Details;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity
public class BandDetailsEntity {

    @Id
    private Long bandId;

    @Nonnull
    private String basedOn, genre,contact;

    private Long charges;
}
