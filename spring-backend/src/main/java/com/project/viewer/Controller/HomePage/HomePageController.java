package com.project.viewer.Controller.HomePage;


import com.project.restro.Entity.RBB.RBPK;
import com.project.viewer.Controller.ViewerBaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/viewers")
public class HomePageController extends ViewerBaseController {

    @GetMapping("/home")
    public ResponseEntity<List> bandsPerformingToday() {
        List<Map<String, Object>> result = viewerService.getAllPerformancesToday();
        return ResponseEntity.ok(result);

    }

    @PostMapping("/performances/updateLikes")
    public ResponseEntity<String> updateLikes(@RequestBody  RBPK rbpk) {

        System.out.println("this is in controller " + rbpk);
        return viewerService.updateLikes(rbpk);
    }

    @PostMapping("/performances/updateDislikes")
    public ResponseEntity<String> updateDislikes(@RequestBody RBPK id) {
        return  viewerService.updateDislike(id);
    }

}
