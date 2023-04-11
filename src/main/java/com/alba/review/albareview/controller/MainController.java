package com.alba.review.albareview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class MainController {
    @GetMapping("/signIn")
    private String signIn(){
        return "signIn";
    }
}
