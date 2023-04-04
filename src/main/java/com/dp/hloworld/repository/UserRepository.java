package com.dp.hloworld.repository;

import com.dp.hloworld.model.User;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Option<User> findByContact(String phone);
    Option<User> findByEmail(String email);

}
