package com.newsroom.controller;

import com.newsroom.dto.ArticleDTO;
import com.newsroom.service.IArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArticleController {
    private final IArticlesService articlesService;
    @PostMapping()
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getTitle() == null || articleDTO.getTitle().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        articlesService.createArticle(articleDTO);
        return new ResponseEntity<>(articleDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getTitle() == null || articleDTO.getTitle().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        articlesService.updateArticle(articleDTO.getId(), articleDTO);
        return new ResponseEntity<>(articleDTO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<ArticleDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(articlesService.findAll(pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleDTO> deleteArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getTitle() == null || articleDTO.getTitle().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        articlesService.deleteArticle(articleDTO.getId());
        return new ResponseEntity<>(articleDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable String id) {
        if (id == null || id.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(articlesService.getArticleById(id), HttpStatus.OK);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Page<ArticleDTO>> getArticleBySlug(@PathVariable String slug,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (slug == null || slug.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(articlesService.getArticleBySlug(slug, pageable), HttpStatus.OK);
    }
}

