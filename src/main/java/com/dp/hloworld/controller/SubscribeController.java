package com.dp.hloworld.controller;

import com.dp.hloworld.helper.JwtUtil;
import com.dp.hloworld.model.*;
import com.dp.hloworld.repository.SubscribeRepository;
import com.dp.hloworld.repository.UserRepository;
import com.dp.hloworld.service.UserService;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@NoArgsConstructor
@RequestMapping(value="/subscribe")
public class SubscribeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/new/{userId}")
    public void save(@PathVariable long userId, HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        Subscribe subscribe = Subscribe.builder().uploaderId(userId).subscriberId(userOptional.get().getId()).build();
        User user = userOptional.get();
        long noOfSubscriber = user.getSubscriber();
        noOfSubscriber++;
        user.setSubscriber(noOfSubscriber);
        userService.saveUser(user);
        subscribeRepository.save(subscribe);
    }

    @GetMapping("/all")
    public List<SubscribeResponse> getSubscriber(HttpServletRequest request) {
        String contactNo = getContact(request);
        Option<User> userOptional = userRepository.findByContact(contactNo);
        List<Subscribe> subscribe = subscribeRepository.findBySubscriberId(userOptional.get().getId());
        List<SubscribeResponse> subscribeResponseList = new ArrayList<>();
        for(Subscribe s: subscribe){
            SubscribeResponse subscribeResponse = SubscribeResponse.builder()
                    .subscriberName(userRepository.findById(s.getSubscriberId()).get().getName()).build();
            subscribeResponseList.add(subscribeResponse);
        }
        return subscribeResponseList;
    }


    public String getContact(HttpServletRequest request) {
        String contact="";
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            if(!jwtUtil.isTokenExpired(jwtToken)){
                Map<String, String> map = jwtUtil.getJwtTokenDetails(request);
                contact= map.get(UserConstants.contactNo);
            }
        }
        return contact;
    }


}
