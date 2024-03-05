package com.ZeroWaveV2.FireAlertV2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "home";  // "home"은 뷰 이름이며, 일반적으로 home.html 파일을 의미합니다.
    }
}

