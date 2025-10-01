package com.newsroom.service;

import com.newsroom.dto.NewsDTO;
import com.newsroom.dto.NewsStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INewsService {
    NewsDTO createNews(NewsDTO newsDTO, String username);
    NewsDTO updateNews(String id, NewsDTO newsDTO);
    NewsDTO getNewsById(String id);
    NewsDTO getNewsBySlug(String slug);
    Page<NewsDTO> getAllPublishedNews(Pageable pageable);
    Page<NewsDTO> getNewsByCategory(String categoryId, Pageable pageable);
    Page<NewsDTO> getFeaturedNews(Pageable pageable);
    Page<NewsDTO> getTrendingNews(Pageable pageable);
    Page<NewsDTO> getAllNews(Pageable pageable);
    Page<NewsDTO> getNewsByStatus(String status, Pageable pageable);
    NewsStatsDTO getNewsStats();
    void deleteNews(String id);
}

