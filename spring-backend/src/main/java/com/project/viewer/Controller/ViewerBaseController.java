package com.project.viewer.Controller;

import com.project.viewer.Service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewerBaseController {

    @Autowired
    protected  ViewerService viewerService;
}
