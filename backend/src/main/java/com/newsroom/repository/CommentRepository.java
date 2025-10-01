package com.newsroom.repository;

import com.newsroom.model.Comment;
import com.newsroom.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByNews(News news, Pageable pageable);
    List<Comment> findByNewsAndApprovedTrue(News news);
    Page<Comment> findByApprovedFalse(Pageable pageable);
}

