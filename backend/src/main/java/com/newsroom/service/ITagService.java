package com.newsroom.service;

import com.newsroom.dto.NewsDTO;
import com.newsroom.dto.TagDTO;
import com.newsroom.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITagService {
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(String id, TagDTO tagDTO);
    TagDTO getTagById(String id);
    TagDTO getTagBySlug(String slug);
    void deleteTag(String id);
    Page<TagDTO> findAll(Pageable  pageable);
}
