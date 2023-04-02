package com.project.band.Repository;

import com.project.band.Entity.Band.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Band_Repository extends JpaRepository<BandEntity, Long> {
    List<BandEntity> findByBandName(String bandName);
    //   List<Band> findByBandNameAndLocation(String bandName, String location);

  //  List<Band> findByEmailAndPassword(String email, String password);

   // List<Band> findByEmail(String email);
}
