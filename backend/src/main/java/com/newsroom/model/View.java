package com.newsroom.model;

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

@Document(collection = "views")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class View {
    @MongoId
    private String id;

    @Field(name = "article_id")
    private String articleId;

    @Field(name = "user_id")
    private String userId;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
