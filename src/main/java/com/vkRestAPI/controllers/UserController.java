package com.vkRestAPI.controllers;

import com.vkRestAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @GetMapping("")
    public String getAllUsers(){
       return userService.getAllUsers();
    }

    @GetMapping("/{userID}")
    public String getOneUserByID(@PathVariable int userID){
       return userService.getOneUserById(userID);
    }


}
