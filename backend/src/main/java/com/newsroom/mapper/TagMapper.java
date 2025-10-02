package com.newsroom.mapper;

import com.newsroom.dto.TagDTO;
import com.newsroom.model.Tag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag>{
    TagDTO toDto(Tag tag);
    Tag toEntity(TagDTO tagDTO);

}
