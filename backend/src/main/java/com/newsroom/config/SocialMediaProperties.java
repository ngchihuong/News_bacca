package com.newsroom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.social-media")
public class SocialMediaProperties {

    private String ytDlpPath = "yt-dlp";

    private Youtube youtube = new Youtube();
    private Facebook facebook = new Facebook();
    private Instagram instagram = new Instagram();

    @Getter
    @Setter
    public static class Youtube {
        private String apiKey;
    }

    @Getter
    @Setter
    public static class Facebook {
        private String accessToken;
    }

    @Getter
    @Setter
    public static class Instagram {
        private String accessToken;
    }
}
