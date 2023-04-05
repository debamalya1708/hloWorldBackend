package com.dp.hloworld.repository;

import com.dp.hloworld.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
}
