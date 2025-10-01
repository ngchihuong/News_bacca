package com.newsroom.controller;

import com.newsroom.dto.NewsDTO;
import com.newsroom.service.INewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NewsController {
    private final INewsService newsService;
    
    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO, 
                                             Authentication authentication) {
        String username = authentication.getName();
        NewsDTO createdNews = newsService.createNews(newsDTO, username);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable String id, 
                                             @Valid @RequestBody NewsDTO newsDTO) {
        NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable String id) {
        NewsDTO news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }
    
    @GetMapping("/slug/{slug}")
    public ResponseEntity<NewsDTO> getNewsBySlug(@PathVariable String slug) {
        NewsDTO news = newsService.getNewsBySlug(slug);
        return ResponseEntity.ok(news);
    }
    
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsDTO> newsPage = newsService.getAllPublishedNews(pageable);
        return ResponseEntity.ok(newsPage);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<NewsDTO>> getNewsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsDTO> newsPage = newsService.getNewsByCategory(categoryId, pageable);
        return ResponseEntity.ok(newsPage);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<Page<NewsDTO>> getFeaturedNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsDTO> newsPage = newsService.getFeaturedNews(pageable);
        return ResponseEntity.ok(newsPage);
    }
    
    @GetMapping("/trending")
    public ResponseEntity<Page<NewsDTO>> getTrendingNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsDTO> newsPage = newsService.getTrendingNews(pageable);
        return ResponseEntity.ok(newsPage);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable String id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}

