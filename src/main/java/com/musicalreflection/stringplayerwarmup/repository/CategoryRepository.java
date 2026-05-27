package com.musicalreflection.stringplayerwarmup.repository;

import com.musicalreflection.stringplayerwarmup.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}