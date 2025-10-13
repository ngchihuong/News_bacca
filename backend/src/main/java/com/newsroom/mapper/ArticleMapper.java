package com.newsroom.mapper;

import com.newsroom.dto.ArticleDTO;
import com.newsroom.dto.TagDTO;
import com.newsroom.model.Article;
import com.newsroom.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<TagDTO, Tag>{
    Article toEntity(ArticleDTO articleDTO);
    ArticleDTO toDTO(Article article);
}
