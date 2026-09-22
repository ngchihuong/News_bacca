package com.newsroom.service.article;

import com.newsroom.dto.ArticleDTO;
import com.newsroom.mapper.ArticleMapper;
import com.newsroom.model.Article;
import com.newsroom.repository.ArticleRepository;
import com.newsroom.service.implement.ArticleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceAuthorArticlesTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    @DisplayName("Thành công: Lấy danh sách bài viết theo tác giả sắp xếp thời gian giảm dần")
    void getArticlesByAuthor_hasArticles_returnsMappedPage() {
        String authorId = "author123";
        Pageable pageable = PageRequest.of(0, 10);

        Article article1 = Article.builder()
                .id("art-1")
                .title("Bài viết công nghệ mới nhất")
                .authorId(authorId)
                .slug("bai-viet-cong-nghe-moi-nhat")
                .createdAt(Instant.now())
                .build();

        ArticleDTO dto1 = new ArticleDTO();
        dto1.setId("art-1");
        dto1.setTitle("Bài viết công nghệ mới nhất");
        dto1.setAuthorId(authorId);
        dto1.setSlug("bai-viet-cong-nghe-moi-nhat");

        Page<Article> mockPage = new PageImpl<>(List.of(article1), pageable, 1);

        when(articleRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable)).thenReturn(mockPage);
        when(articleMapper.toDTO(article1)).thenReturn(dto1);

        Page<ArticleDTO> result = articleService.getArticlesByAuthor(authorId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("art-1", result.getContent().get(0).getId());
        assertEquals("Bài viết công nghệ mới nhất", result.getContent().get(0).getTitle());

        verify(articleRepository, times(1)).findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);
        verify(articleMapper, times(1)).toDTO(article1);
    }

    @Test
    @DisplayName("Thành công: Tác giả chưa có bài viết trả về Page rỗng")
    void getArticlesByAuthor_noArticles_returnsEmptyPage() {
        String authorId = "author-no-posts";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Article> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(articleRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable)).thenReturn(emptyPage);

        Page<ArticleDTO> result = articleService.getArticlesByAuthor(authorId, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(articleRepository, times(1)).findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);
        verify(articleMapper, never()).toDTO(any());
    }
}
