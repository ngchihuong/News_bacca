package com.newsroom.service.Impl;

import com.newsroom.dto.TagDTO;
import com.newsroom.mapper.TagMapper;
import com.newsroom.model.Tag;
import com.newsroom.repository.TagRepository;
import com.newsroom.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

    private final TagMapper tagMapper;
    private final TagRepository tagRepository;

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO updateTag(String id, TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO getTagById(String id) {
        return tagRepository.findById(id).map(tagMapper::toDto).orElse(null);
    }

    @Override
    public Page<TagDTO> getTagBySlug(String slug, Pageable pageable) {
        return tagRepository.findBySlugContainingIgnoreCase(slug, pageable).map(tagMapper::toDto);
    }

    @Override
    public void deleteTag(String id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(tagMapper::toDto);
    }
}
