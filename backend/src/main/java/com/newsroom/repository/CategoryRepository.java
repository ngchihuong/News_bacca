package com.newsroom.repository;

import com.newsroom.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);
    List<Category> findByActiveTrue();
}

