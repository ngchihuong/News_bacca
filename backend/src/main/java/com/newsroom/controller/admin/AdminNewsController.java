package com.newsroom.controller.admin;

import com.newsroom.dto.NewsDTO;
import com.newsroom.dto.NewsStatsDTO;
import com.newsroom.service.INewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
public class AdminNewsController {
    
    private final INewsService newsService;
    
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsDTO> newsPage;
        
        if (status != null && !status.isEmpty()) {
            newsPage = newsService.getNewsByStatus(status, pageable);
        } else {
            newsPage = newsService.getAllNews(pageable);
        }
        
        return ResponseEntity.ok(newsPage);
    }
    
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
    
    @PutMapping("/{id}/publish")
    public ResponseEntity<NewsDTO> publishNews(@PathVariable String id) {
        NewsDTO newsDTO = newsService.getNewsById(id);
        newsDTO.setStatus("PUBLISHED");
        NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }
    
    @PutMapping("/{id}/unpublish")
    public ResponseEntity<NewsDTO> unpublishNews(@PathVariable String id) {
        NewsDTO newsDTO = newsService.getNewsById(id);
        newsDTO.setStatus("DRAFT");
        NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable String id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<NewsStatsDTO> getNewsStats() {
        NewsStatsDTO stats = newsService.getNewsStats();
        return ResponseEntity.ok(stats);
    }
}

