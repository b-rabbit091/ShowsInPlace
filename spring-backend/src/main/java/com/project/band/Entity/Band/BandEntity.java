package com.project.band.Entity.Band;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
public class BandEntity {

    @Id
    Long bandId;
    @Nonnull
    String bandName;


}

