package com.newsroom.controller;

import com.newsroom.dto.TagDTO;
import com.newsroom.repository.TagRepository;
import com.newsroom.service.INewsService;
import com.newsroom.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/Tag")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TagController {
    private final ITagService tagService;

    @PostMapping("")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       }
        TagDTO tag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }
}
