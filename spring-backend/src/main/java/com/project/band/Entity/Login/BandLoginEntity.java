package com.project.band.Entity.Login;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
public class BandLoginEntity {

    @Id
    private Long bandId;
    @Nonnull
    private String email;
    @Nonnull
    private String password;

}
