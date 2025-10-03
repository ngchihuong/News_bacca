package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "article_tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleTag {
    @Field(name = "article_id")
    private String articleId;

    @Field(name = "tag_id")
    private String tagId;

}
