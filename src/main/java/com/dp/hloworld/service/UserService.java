package com.dp.hloworld.service;

import com.dp.hloworld.model.User;
import com.dp.hloworld.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        log.info("#  sign up in user with mobile from service - {}", user);
        return userRepository.save(user);
    }
}
