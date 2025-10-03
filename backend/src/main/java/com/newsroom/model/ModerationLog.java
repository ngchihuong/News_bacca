package com.newsroom.model;

import com.newsroom.enums.TargetType;
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

@Document(collection = "moderation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationLog {

    @MongoId
    private String id;

    @Field(name = "moderator_id")
    private String moderatorId; // Người kiểm duyệt

    private String action;  //hành động sửa, xóa,...

    @Field(name = "target_type")
    private TargetType targetType;

    @Field(name = "target_id")
    private String targetId; //	ID của đối tượng

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
