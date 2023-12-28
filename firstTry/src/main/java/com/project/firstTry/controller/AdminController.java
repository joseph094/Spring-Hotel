package com.project.firstTry.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/you")

    public ResponseEntity<String> sayHello(){

        return ResponseEntity.ok("Hi Admin you");
    }
    @GetMapping

    public ResponseEntity<String> say(){

        return ResponseEntity.ok("Hi Admin");
    }
}
