package com.example.dummy_api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
@CrossOrigin(origins = "http://localhost:5173")
public class UserPointRestController {

    private final UserPointService userPointService;

    public UserPointRestController(UserPointService userPointService) {
        this.userPointService = userPointService;
    }

    @GetMapping
    public List<User> getUserPoints() {
        return userPointService.generateRandomUsers(100); // 100人分生成
    }
}
