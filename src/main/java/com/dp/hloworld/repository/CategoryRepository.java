package com.dp.hloworld.repository;

import com.dp.hloworld.model.Category;
import com.dp.hloworld.model.Video;
import com.dp.hloworld.model.VideoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
