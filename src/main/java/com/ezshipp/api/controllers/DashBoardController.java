package com.ezshipp.api.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by srinivasseri on 3/3/18.
 */
@RestController
@Api(value = "/api/dashboard/", description = "a rest service")
@RequestMapping(path = "/api/dashboard/")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class DashBoardController implements ControllerConstants {
}
