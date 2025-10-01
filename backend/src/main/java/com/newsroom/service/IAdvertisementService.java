package com.newsroom.service;

import com.newsroom.dto.AdvertisementDTO;

import java.util.List;

public interface IAdvertisementService {
    AdvertisementDTO createAdvertisement(AdvertisementDTO dto);
    AdvertisementDTO updateAdvertisement(String id, AdvertisementDTO dto);
    AdvertisementDTO getAdvertisementById(String id);
    List<AdvertisementDTO> getAllAdvertisements();
    List<AdvertisementDTO> getActiveAdvertisements();
    List<AdvertisementDTO> getAdvertisementsByPosition(String position);
    void incrementImpression(String id);
    void incrementClick(String id);
    void deleteAdvertisement(String id);
}

