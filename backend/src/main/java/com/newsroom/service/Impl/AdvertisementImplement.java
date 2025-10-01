package com.newsroom.service.Impl;

import com.newsroom.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementImplement {
    private final AdvertisementRepository advertisementRepository;
}
