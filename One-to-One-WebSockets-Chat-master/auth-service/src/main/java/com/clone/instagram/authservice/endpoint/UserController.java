package com.clone.instagram.authservice.endpoint;

import com.clone.instagram.authservice.exception.BadRequestException;
import com.clone.instagram.authservice.exception.EmailAlreadyExistsException;
import com.clone.instagram.authservice.exception.ResourceNotFoundException;
import com.clone.instagram.authservice.exception.UsernameAlreadyExistsException;
import com.clone.instagram.authservice.model.InstaUserDetails;
import com.clone.instagram.authservice.model.Profile;
import com.clone.instagram.authservice.model.Role;
import com.clone.instagram.authservice.model.User;
import com.clone.instagram.authservice.payload.ApiResponse;
import com.clone.instagram.authservice.payload.SignUpRequest;
import com.clone.instagram.authservice.payload.UserSummary;
import com.clone.instagram.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/users/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllUserSummaries(
            @AuthenticationPrincipal InstaUserDetails userDetails) {
        log.info("retrieving all users summaries");
        return ResponseEntity.ok(userService.allSumaries(userDetails));
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('FACEBOOK_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSummary getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        return UserSummary
                .builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .name(userDetails.getUserProfile().getDisplayName())
                .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
                .build();
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return ResponseEntity.ok(userService.userSummary(username));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getUsername());
        try {
           User user = userService.registerUser(payload, Role.USER);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(user.getUsername()).toUri();

            return ResponseEntity
                    .created(location)
                    .body(new ApiResponse(true, "User registered successfully"));
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@Valid @RequestBody SignUpRequest payload, @PathVariable String userId) {
        User user =  userService.updateProfileUrl(payload,userId);
        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "User registered successfully"));

    }
}