package com.priyanka.accesshub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }
}
