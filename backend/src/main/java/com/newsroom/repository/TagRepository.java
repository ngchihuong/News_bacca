package com.newsroom.repository;

import com.newsroom.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findBySlug(String slug);
    Optional<Tag> findByName(String name);
    List<Tag> findByNameIn(List<String> names);
}

