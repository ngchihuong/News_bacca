package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    private String id;
    
    @TextIndexed
    private String title;
    
    @Indexed(unique = true)
    private String slug;
    
    private String excerpt;
    
    @TextIndexed
    private String content;
    
    private String imageUrl;
    
    @DBRef
    private Category category;
    
    @DBRef
    private List<Tag> tags;
    
    @DBRef
    private User author;
    
    private int viewCount = 0;
    private boolean featured = false;
    private boolean trending = false;
    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED
    
    private LocalDateTime publishedAt;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

