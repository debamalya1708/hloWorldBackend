package com.dp.hloworld.repository;

import com.dp.hloworld.model.Likes;
import com.dp.hloworld.model.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewsRepository extends JpaRepository<Views,Long> {

    @Query(value = "SELECT l.videoId FROM Views l WHERE l.userId = :id")
    List<Long> findByUserId(@Param("id")long id);

    Optional<Views> findByUserIdAndVideoId(long userid, long videoId);
}
