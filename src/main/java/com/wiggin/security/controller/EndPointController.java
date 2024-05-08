package com.wiggin.security.controller;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class EndPointController {


    @GetMapping
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("This is home");
    }
}
