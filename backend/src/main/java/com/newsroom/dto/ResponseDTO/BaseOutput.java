package com.newsroom.dto.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.newsroom.enums.ResponseStatus;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseOutput<T> {
    List<String> errors;
    String message;
    Meta meta;
    ResponseStatus statusCode;
    T data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Meta {
        private Integer currentPage;
        private Integer pageSize;
        private Long totalPages;
        private Long total;
    }
}
