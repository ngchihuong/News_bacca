package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;
    
    @DBRef
    private News news;
    
    @DBRef
    private User user;
    
    private String content;
    private String parentId; // For nested comments
    private boolean approved = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
}

