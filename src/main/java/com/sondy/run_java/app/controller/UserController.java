package com.sondy.run_java.app.controller;

import com.sondy.run_java.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public HashMap<String, Object> insertUser (@RequestBody HashMap<String,Object> paramMap) {
        HashMap<String,Object> result = new HashMap<>();
        System.out.println(paramMap.toString());
        System.out.println(paramMap.toString());
        System.out.println(paramMap.toString());
        result.put("result",userService.insertUser(paramMap));

        return result;
    }

    @GetMapping("/history")
    public HashMap<String,Object> getUserRunHistory(@RequestParam HashMap<String,Object> paramMap){
        return userService.getUserRunHistory(paramMap);
    }

    @GetMapping("/history/detail")
    public HashMap<String,Object> getUserRunHistoryDetail(@RequestParam HashMap<String,Object> paramMap){
        return userService.getUserRunHistoryDetail(paramMap);
    }
}
