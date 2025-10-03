package com.newsroom.model;

import com.newsroom.enums.RoleType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Role {
    @MongoId
    private String id;

    @Field(name = "role_type")
    private RoleType roleType;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
