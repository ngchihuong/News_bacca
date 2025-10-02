package com.newsroom.model;

import com.newsroom.enums.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @MongoId
    private String id;

    private String title;

    private String content;

    @Field(name = "author_id")
    private String authorId;

    @Field(name = "category_id")
    private String categoryId;

    private ArticleType type;

    private String slug;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
