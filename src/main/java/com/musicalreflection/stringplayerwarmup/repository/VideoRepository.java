package com.musicalreflection.stringplayerwarmup.repository;

import com.musicalreflection.stringplayerwarmup.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    // Native SQLite query to get one random video from a specific category
    @Query(value = "SELECT * FROM video WHERE category_id = :categoryId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Video> findRandomVideoByCategoryId(@Param("categoryId") Long categoryId);
}