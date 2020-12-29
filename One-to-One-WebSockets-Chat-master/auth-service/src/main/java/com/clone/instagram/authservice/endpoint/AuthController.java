package com.clone.instagram.authservice.endpoint;


import com.clone.instagram.authservice.exception.BadRequestException;
import com.clone.instagram.authservice.exception.EmailAlreadyExistsException;
import com.clone.instagram.authservice.exception.ResourceNotFoundException;
import com.clone.instagram.authservice.exception.UsernameAlreadyExistsException;
import com.clone.instagram.authservice.model.Profile;
import com.clone.instagram.authservice.model.Role;
import com.clone.instagram.authservice.model.User;
import com.clone.instagram.authservice.payload.*;
import com.clone.instagram.authservice.service.FacebookService;
import com.clone.instagram.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private FacebookService facebookService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        log.info("facebook login {}", facebookLoginRequest);
        String token = facebookService.loginUser(facebookLoginRequest.getAccessToken());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

}
