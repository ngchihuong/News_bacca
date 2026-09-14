package com.newsroom.service.implement.downloadCollectionsImpl.fetcher;

import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.enums.SocialPlatform;
import com.newsroom.service.downloadCollecttionService.SocialVideoFetcher;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SocialVideoFetcherFactory {

    private final Map<SocialPlatform, SocialVideoFetcher> fetchers;

    public SocialVideoFetcherFactory(List<SocialVideoFetcher> fetcherList) {
        this.fetchers = new EnumMap<>(SocialPlatform.class);
        for (SocialVideoFetcher fetcher : fetcherList) {
            this.fetchers.put(fetcher.getPlatform(), fetcher);
        }
    }

    public SocialVideoFetcher getFetcher(SocialPlatform platform) {
        SocialVideoFetcher fetcher = fetchers.get(platform);
        if (fetcher == null) {
            throw new NewsCommonException("error.social.platform.unsupported");
        }
        return fetcher;
    }
}
