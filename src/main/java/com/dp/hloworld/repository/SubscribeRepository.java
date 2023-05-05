package com.dp.hloworld.repository;

import com.dp.hloworld.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe,Long> {

    List<Subscribe> findBySubscriberId(long id);

    @Query(value = "SELECT l.uploaderId FROM Subscribe l WHERE l.subscriberId = :id")
    List<Long> findAllUploaderIdBySubscriberId(@Param("id")long id);
}
