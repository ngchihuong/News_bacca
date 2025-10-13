package com.newsroom.service.implement;

import com.newsroom.dto.ArticleDTO;
import com.newsroom.mapper.ArticleMapper;
import com.newsroom.model.Article;
import com.newsroom.repository.ArticleRepository;
import com.newsroom.service.IArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticlesService {

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDTO(article);
    }

    @Override
    public ArticleDTO updateArticle(String id, ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDTO(article);
    }

    @Override
    public ArticleDTO getArticleById(String id) {
        return articleRepository.findById(id).map(articleMapper::toDTO).orElse(null);
    }

    @Override
    public Page<ArticleDTO> getArticleBySlug(String slug, Pageable pageable) {
        return articleRepository.findBySlugContainingIgnoreCase(slug, pageable).map(articleMapper::toDTO);
    }

    @Override
    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
    }

    @Override
    public Page<ArticleDTO> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toDTO);
    }
}
