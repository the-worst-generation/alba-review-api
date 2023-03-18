package com.alba.review.albareview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/test")
    public String hello(){
        return "Hello World!";
    }
    @GetMapping("/")
    public String main(){
        return "main";
    }
    @GetMapping("/loginFail")
    public String fail(){
        return "fail";
    }
}
