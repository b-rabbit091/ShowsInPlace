package com.project.band.Repository.Details;

import com.project.band.Entity.Details.BandDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandDetailsRepository extends JpaRepository<BandDetailsEntity,Long> {




}
