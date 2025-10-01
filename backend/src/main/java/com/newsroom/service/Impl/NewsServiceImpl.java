package com.newsroom.service.Impl;

import com.newsroom.dto.NewsDTO;
import com.newsroom.dto.NewsStatsDTO;
import com.newsroom.model.News;
import com.newsroom.model.Category;
import com.newsroom.model.Tag;
import com.newsroom.model.User;
import com.newsroom.repository.NewsRepository;
import com.newsroom.repository.CategoryRepository;
import com.newsroom.repository.TagRepository;
import com.newsroom.repository.UserRepository;
import com.newsroom.service.INewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements INewsService {
    
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public NewsDTO createNews(NewsDTO newsDTO, String username) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setSlug(generateSlug(newsDTO.getTitle()));
        news.setExcerpt(newsDTO.getExcerpt());
        news.setContent(newsDTO.getContent());
        news.setImageUrl(newsDTO.getImageUrl());
        news.setStatus(newsDTO.getStatus() != null ? newsDTO.getStatus() : "DRAFT");
        news.setFeatured(newsDTO.isFeatured());
        news.setTrending(newsDTO.isTrending());
        
        // Set category
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        news.setCategory(category);
        
        // Set tags if provided
        if (newsDTO.getTagIds() != null && !newsDTO.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(newsDTO.getTagIds());
            news.setTags(tags);
        }
        
        // Set author
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        news.setAuthor(author);
        
        if ("PUBLISHED".equals(news.getStatus())) {
            news.setPublishedAt(LocalDateTime.now());
        }
        
        News savedNews = newsRepository.save(news);
        return convertToDTO(savedNews);
    }
    
    @Override
    @Transactional
    public NewsDTO updateNews(String id, NewsDTO newsDTO) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        news.setTitle(newsDTO.getTitle());
        news.setSlug(generateSlug(newsDTO.getTitle()));
        news.setExcerpt(newsDTO.getExcerpt());
        news.setContent(newsDTO.getContent());
        news.setImageUrl(newsDTO.getImageUrl());
        news.setFeatured(newsDTO.isFeatured());
        news.setTrending(newsDTO.isTrending());
        
        // Update category
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        news.setCategory(category);
        
        // Update tags
        if (newsDTO.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(newsDTO.getTagIds());
            news.setTags(tags);
        }
        
        // Update publish status
        String oldStatus = news.getStatus();
        news.setStatus(newsDTO.getStatus());
        if ("PUBLISHED".equals(newsDTO.getStatus()) && !"PUBLISHED".equals(oldStatus)) {
            news.setPublishedAt(LocalDateTime.now());
        }
        
        News updatedNews = newsRepository.save(news);
        return convertToDTO(updatedNews);
    }
    
    @Override
    public NewsDTO getNewsById(String id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        return convertToDTO(news);
    }
    
    @Override
    public NewsDTO getNewsBySlug(String slug) {
        News news = newsRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        // Increment view count
        news.setViewCount(news.getViewCount() + 1);
        newsRepository.save(news);
        
        return convertToDTO(news);
    }
    
    @Override
    public Page<NewsDTO> getAllPublishedNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findByStatusOrderByCreatedAtDesc("PUBLISHED", pageable);
        return newsPage.map(this::convertToDTO);
    }
    
    @Override
    public Page<NewsDTO> getNewsByCategory(String categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Page<News> newsPage = newsRepository.findByStatusAndCategoryOrderByCreatedAtDesc("PUBLISHED", category, pageable);
        return newsPage.map(this::convertToDTO);
    }
    
    @Override
    public Page<NewsDTO> getFeaturedNews(Pageable pageable) {
        return newsRepository.findByFeaturedTrue(pageable).map(this::convertToDTO);
    }
    
    @Override
    public Page<NewsDTO> getTrendingNews(Pageable pageable) {
        return newsRepository.findByTrendingTrue(pageable).map(this::convertToDTO);
    }
    
    @Override
    public Page<NewsDTO> getAllNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findAll(pageable);
        return newsPage.map(this::convertToDTO);
    }
    
    @Override
    public Page<NewsDTO> getNewsByStatus(String status, Pageable pageable) {
        Page<News> newsPage = newsRepository.findByStatus(status, pageable);
        return newsPage.map(this::convertToDTO);
    }
    
    @Override
    public NewsStatsDTO getNewsStats() {
        long totalNews = newsRepository.count();
        long publishedNews = newsRepository.countByStatus("PUBLISHED");
        long draftNews = newsRepository.countByStatus("DRAFT");
        long featuredNews = newsRepository.countByFeaturedTrue();
        
        NewsStatsDTO stats = new NewsStatsDTO();
        stats.setTotalNews(totalNews);
        stats.setPublishedNews(publishedNews);
        stats.setDraftNews(draftNews);
        stats.setFeaturedNews(featuredNews);
        
        return stats;
    }
    
    @Override
    @Transactional
    public void deleteNews(String id) {
        newsRepository.deleteById(id);
    }
    
    private NewsDTO convertToDTO(News news) {
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSlug(news.getSlug());
        dto.setExcerpt(news.getExcerpt());
        dto.setContent(news.getContent());
        dto.setImageUrl(news.getImageUrl());
        dto.setViewCount(news.getViewCount());
        dto.setFeatured(news.isFeatured());
        dto.setTrending(news.isTrending());
        dto.setStatus(news.getStatus());
        dto.setPublishedAt(news.getPublishedAt());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        
        if (news.getCategory() != null) {
            dto.setCategoryId(news.getCategory().getId());
            dto.setCategoryName(news.getCategory().getName());
        }
        
        if (news.getTags() != null) {
            dto.setTagIds(news.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
            dto.setTagNames(news.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        }
        
        if (news.getAuthor() != null) {
            dto.setAuthorId(news.getAuthor().getId());
            dto.setAuthorName(news.getAuthor().getFullName());
        }
        
        return dto;
    }
    
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}

