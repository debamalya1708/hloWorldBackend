package com.dp.hloworld.controller;

import com.dp.hloworld.config.JwtResponse;
import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.*;
import com.dp.hloworld.repository.FavouriteRepository;
import com.dp.hloworld.repository.LikeRepository;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.repository.ViewsRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    private final LikeRepository likeRepository;
    private final ViewsRepository viewsRepository;
    private final FavouriteRepository favouriteRepository;

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


    @GetMapping("/check")
    public ResponseEntity<?> checkUser(HttpServletRequest request){
        log.info("request length: {}", request.getContentLength());
        if(request.getContentLength()==0){
            return  new ResponseEntity<>("token Not Found!!!",HttpStatus.NOT_FOUND);
        }
        log.info("request: {}", request);
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            if(jwtUtil.isTokenExpired(jwtToken)){
                return  new ResponseEntity<>("User Not Found!!!",HttpStatus.NOT_FOUND);
            }
            else if(!jwtUtil.isTokenExpired(jwtToken)){
                Map<String, String> map = jwtUtil.getJwtTokenDetails(request);
                Option<User> userOptional = userRepository.findByContact(map.get(UserConstants.contactNo));
                List<Long> viewsList = viewsRepository.findByUserId(userOptional.get().getId());
                List<Long> likesList = likeRepository.findByUserId(userOptional.get().getId());
                List<Long> favouriteList = favouriteRepository.findByUserId(userOptional.get().getId());

                UserResponse userResponse = UserResponse.builder().id(userOptional.get().getId())
                        .name(userOptional.get().getName())
                        .contact(userOptional.get().getContact())
                        .email(userOptional.get().getEmail())
                        .likesList(likesList)
                        .viewsList(viewsList)
                        .favouritesList(favouriteList)
                        .build();
                if (!userOptional.isEmpty())
                    return new ResponseEntity<>(userResponse,HttpStatus.OK);
                else
                    return  new ResponseEntity<>("User Not Found!!!",HttpStatus.NOT_FOUND);
            }

        }
        return new ResponseEntity<>("No Key found",HttpStatus.UNAUTHORIZED);
    }

}
