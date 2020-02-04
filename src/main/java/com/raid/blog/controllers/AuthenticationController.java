package com.raid.blog.controllers;

import com.raid.blog.models.User;
import com.raid.blog.services.UserService;
import com.raid.blog.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.HashMap;


@Controller
public class AuthenticationController {

    @Autowired
    private UserService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/async-login")
    public String asyncLogin(){
        return "users/async-login";
    }

    @PostMapping("/async-login")
    @ResponseBody
    public HashMap<String, String> doLogin(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password){
        HashMap<String, String> response = new HashMap<>();
        User dbUser = usersRepository.findByUsername(username);
        if(dbUser != null && passwordEncoder.matches(password, dbUser.getPassword())) {
            usersService.authenticate(dbUser);
            response.put("status", "ok");
        }else{
            response.put("status", "error");
        }
        return response;
    }

    @GetMapping("/register")
    public String showForm(Model m){
        m.addAttribute("user", new User());
        return "users/create";
    }

}
