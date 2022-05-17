package com.example.bezbednost.bezbednost.controller;

import com.example.bezbednost.bezbednost.dto.UserDto;
import com.example.bezbednost.bezbednost.iservice.IUserService;
import com.example.bezbednost.bezbednost.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public User user(Principal user) {
        return userService.findByUsername(user.getName());
    }

    @PostMapping()
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
        userService.save(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/isUserRegistered")
    public boolean isUserRegistered(@RequestParam String username){
        return userService.isUserRegistered(username);
    }

    @GetMapping("/getRole/{id}")
    public String getUserRole(@PathVariable Integer id) { return this.userService.findUserRole(id); }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        boolean verified = userService.verify(code);

        return verified ? "Your account is successfully verified!" : "We are sorry, your account is not verified.";
    }
}
