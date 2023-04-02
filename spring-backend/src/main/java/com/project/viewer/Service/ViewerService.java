package com.project.viewer.Service;

import com.project.restro.Entity.RBB.RBPK;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ViewerService {
    List<Map<String, Object>> getAllPerformancesToday();

    ResponseEntity<String> updateLikes(RBPK id);

    ResponseEntity<String>  updateDislike(RBPK rbpk);
}
