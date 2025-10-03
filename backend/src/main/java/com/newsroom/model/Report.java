package com.newsroom.model;

import com.newsroom.enums.ReportStatus;
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

@Document(collection = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    
    @MongoId
    private String id;
    
    private TargetType targetType;
    
    @Field(name = "target_id")
    private String targetId;  //ID của đối tượng bị báo cáo

    @Field(name = "reporter_id")
    private String reporterId; //ID của nguời báo cáo

    private String reason; //lý do
    
    private ReportStatus status = ReportStatus.PENDING;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
