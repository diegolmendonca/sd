package com.clone.instagram.authservice.service;

import com.clone.instagram.authservice.exception.EmailAlreadyExistsException;
import com.clone.instagram.authservice.exception.ResourceNotFoundException;
import com.clone.instagram.authservice.exception.UsernameAlreadyExistsException;
import com.clone.instagram.authservice.model.InstaUserDetails;
import com.clone.instagram.authservice.model.Profile;
import com.clone.instagram.authservice.model.Role;
import com.clone.instagram.authservice.model.User;
import com.clone.instagram.authservice.payload.SignUpRequest;
import com.clone.instagram.authservice.payload.UserSummary;
import com.clone.instagram.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;


    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return tokenProvider.generateToken(authentication);
    }

    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>() {{
            add(role);
        }});

        return saveUser(user);
    }

    public User registerUser(SignUpRequest payload, Role role) {
        User user = User
                .builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .gender(payload.getGender())
                .userProfile(Profile
                        .builder()
                        .displayName(payload.getName())
                        .profilePictureUrl(payload.getProfilePicUrl())
                        .build())
                .build();

        return registerUser(user, role);
    }

    public User updateProfileUrl(SignUpRequest payload,String userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found for this id: " + userId));

        Profile profle = user.getUserProfile();
        profle.setProfilePictureUrl(payload.getProfilePicUrl());

        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");

        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        log.info("retrieving user {}", username);

        Optional<User> optUser = userRepository.findByUsername(username);
        return optUser.orElseThrow(() -> new ResourceNotFoundException(username));

    }

    public Optional<User> findById(String id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }


    public UserSummary userSummary(String username) {
        return convertTo(findByUsername(username));
    }

    public List<UserSummary> allSumaries(InstaUserDetails userDetails) {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(userDetails.getUsername()))
                .map(this::convertTo).collect(Collectors.toList());
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl())
                .build();
    }
}
