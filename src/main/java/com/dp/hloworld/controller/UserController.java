package com.dp.hloworld.controller;

import com.dp.hloworld.config.JwtResponse;
import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.LogIn;
import com.dp.hloworld.model.User;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.service.CustomUserDetailsService;
import com.dp.hloworld.service.UserService;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    private final UserService userService;


    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        log.info("#  sign up in user with mobile - {}", user);
        Option<User> userOptional = userRepository.findByContact(user.getContact());
        Option<User> singleUser = userRepository.findByEmail(user.getEmail());

        if(!userOptional.isEmpty()){
            return new ResponseEntity<>("User with given Contact No. already exists", HttpStatus.BAD_REQUEST);
        }
        if(!singleUser.isEmpty()){
            return new ResponseEntity<>("User with given email already exists", HttpStatus.BAD_REQUEST);
        }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User res = userService.saveUser(user);
            if(res.getId() > 0){
                return new ResponseEntity<>("User Registered",HttpStatus.OK);
            }

        return new ResponseEntity<>("Not authorized to sign up!!!",HttpStatus.BAD_REQUEST);

    }


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LogIn login){
        Option<User> userOptional = userRepository.findByContact(login.getUserName());
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
        }
        log.info("#  log in user with mobile - {}", login);
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(),
                    login.getPassword()));
        }
        catch(BadCredentialsException ex) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.FORBIDDEN);
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(login.getUserName());
        Option<User> user  = userRepository.findByContact(login.getUserName());
        final String jwt = jwtUtil.generateToken(user.get(),userDetails);
        return new ResponseEntity<>(new JwtResponse(jwt),HttpStatus.OK);
    }

}
