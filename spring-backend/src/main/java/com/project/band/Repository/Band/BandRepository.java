package com.project.band.Repository.Band;

import com.project.band.Entity.Band.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandRepository extends JpaRepository<BandEntity,Long> {
}
