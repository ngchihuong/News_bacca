package com.newsroom.model;

import com.newsroom.enums.ReactionType;
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
import java.time.LocalDateTime;

@Data
@Document(collection = "reactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {
    @MongoId
    private String id;

    @Field(name = "article_id")
    private String articleId;

    @Field(name = "user_id")
    private String userId;

    private ReactionType type;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
