package com.priyanka.accesshub.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Hidden
@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }
}
