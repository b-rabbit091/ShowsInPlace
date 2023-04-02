package com.project.band.Repository.Login;

import com.project.band.Entity.Login.BandLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandLoginRepository extends JpaRepository<BandLoginEntity,Long> {
    List<BandLoginEntity> findByEmail(String email);
}
