package com.dp.hloworld.repository;

import com.dp.hloworld.model.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewsRepository extends JpaRepository<Views,Long> {
}
