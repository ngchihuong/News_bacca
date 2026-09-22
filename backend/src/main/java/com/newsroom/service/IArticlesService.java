package com.newsroom.service;

import com.newsroom.dto.ArticleDTO;
import com.newsroom.dto.TagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IArticlesService {
    ArticleDTO createArticle(ArticleDTO articleDTO);
    ArticleDTO updateArticle(String id, ArticleDTO articleDTO);
    ArticleDTO getArticleById(String id);
    Page<ArticleDTO> getArticleBySlug(String slug, Pageable pageable);
    void deleteArticle(String id);
    Page<ArticleDTO> findAll(Pageable pageable);
    Page<ArticleDTO> getArticlesByAuthor(String authorId, Pageable pageable);
}
