package com.newsroom.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin {
        private String type = "Bearer";
        private String id;
        private String name;
        private List<String> roles;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private String id;
        private String name;
        private String email;
    }
}
