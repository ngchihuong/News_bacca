package com.newsroom.controller;

import com.newsroom.commons.Constants;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.NewsDTO;
import com.newsroom.dto.TagDTO;
import com.newsroom.service.ITagService;
import com.newsroom.config.exceptions.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Tag")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TagController {
    private final ITagService tagService;

    @PostMapping("")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO.getId() != null) {
            throw new NewsCommonException(Constants.ERROR.CATEGORY.NOT_EXIST);
       }
        TagDTO tag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable String id, @RequestBody TagDTO tagDTO) {
        if (tagDTO.getId() == null) {
            throw new NewsCommonException(Constants.ERROR.CATEGORY.NOT_EXIST);
        }
        TagDTO tag = tagService.updateTag(id, tagDTO);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<TagDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TagDTO> newsPage = tagService.findAll(pageable);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTag(@PathVariable String id) {
        if (id == null) {
            throw new NewsCommonException(Constants.ERROR.CATEGORY.NOT_EXIST);
        }
        TagDTO tag = tagService.getTagById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDTO> deleteTag(@PathVariable String id) {
        if (id == null) {
            throw new NewsCommonException(Constants.ERROR.CATEGORY.NOT_EXIST);
        }
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Page<TagDTO>> getTagBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TagDTO> tag = tagService.getTagBySlug(slug, pageable);
        return ResponseEntity.ok(tag);
    }


}
