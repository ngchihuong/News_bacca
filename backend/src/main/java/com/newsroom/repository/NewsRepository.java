package com.newsroom.repository;

import com.newsroom.model.News;
import com.newsroom.model.Category;
import com.newsroom.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
    Optional<News> findBySlug(String slug);
    Page<News> findByStatus(String status, Pageable pageable);
    Page<News> findByCategory(Category category, Pageable pageable);
    Page<News> findByTagsIn(List<Tag> tags, Pageable pageable);
    Page<News> findByFeaturedTrue(Pageable pageable);
    Page<News> findByTrendingTrue(Pageable pageable);
    Page<News> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    Page<News> findByStatusAndCategoryOrderByCreatedAtDesc(String status, Category category, Pageable pageable);
    
    long countByStatus(String status);
    long countByFeaturedTrue();
}

