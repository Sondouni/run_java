package com.sondy.run_java.app.controller;

import com.sondy.run_java.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public HashMap<String, Object> getUserList () {
        HashMap<String,Object> result = new HashMap<>();

        result = userService.getUserList();

        return result;
    }
}
