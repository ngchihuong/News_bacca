package com.newsroom.service;

import com.newsroom.dto.AdvertisementDTO;
import com.newsroom.model.Advertisement;
import com.newsroom.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    
    private final AdvertisementRepository advertisementRepository;
    
    @Transactional
    public AdvertisementDTO createAdvertisement(AdvertisementDTO dto) {
        Advertisement ad = new Advertisement();
        mapDtoToEntity(dto, ad);
        
        Advertisement savedAd = advertisementRepository.save(ad);
        return convertToDTO(savedAd);
    }
    
    @Transactional
    public AdvertisementDTO updateAdvertisement(String id, AdvertisementDTO dto) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        
        mapDtoToEntity(dto, ad);
        
        Advertisement updatedAd = advertisementRepository.save(ad);
        return convertToDTO(updatedAd);
    }
    
    public AdvertisementDTO getAdvertisementById(String id) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        return convertToDTO(ad);
    }
    
    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AdvertisementDTO> getActiveAdvertisements() {
        return advertisementRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AdvertisementDTO> getAdvertisementsByPosition(String position) {
        LocalDateTime now = LocalDateTime.now();
        
        List<Advertisement> ads = advertisementRepository
                .findByActiveTrueAndPositionAndStartDateBeforeAndEndDateAfter(
                    position, now, now
                );
        
        // Fallback: get active ads by position without date check if no scheduled ads
        if (ads.isEmpty()) {
            ads = advertisementRepository.findByPositionAndActiveTrue(position);
        }
        
        return ads.stream()
                .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void incrementImpression(String id) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ad.setImpressions(ad.getImpressions() + 1);
        advertisementRepository.save(ad);
    }
    
    @Transactional
    public void incrementClick(String id) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ad.setClicks(ad.getClicks() + 1);
        advertisementRepository.save(ad);
    }
    
    @Transactional
    public void deleteAdvertisement(String id) {
        advertisementRepository.deleteById(id);
    }
    
    private void mapDtoToEntity(AdvertisementDTO dto, Advertisement ad) {
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setAdType(dto.getAdType());
        ad.setPosition(dto.getPosition());
        ad.setFormat(dto.getFormat());
        ad.setImageUrl(dto.getImageUrl());
        ad.setHtmlContent(dto.getHtmlContent());
        ad.setScriptCode(dto.getScriptCode());
        ad.setTargetUrl(dto.getTargetUrl());
        ad.setOpenInNewTab(dto.isOpenInNewTab());
        ad.setWidth(dto.getWidth());
        ad.setHeight(dto.getHeight());
        ad.setStartDate(dto.getStartDate());
        ad.setEndDate(dto.getEndDate());
        ad.setActive(dto.isActive());
        ad.setPriority(dto.getPriority());
        ad.setDisplayFrequency(dto.getDisplayFrequency());
        ad.setMaxDailyImpressions(dto.getMaxDailyImpressions());
    }
    
    private AdvertisementDTO convertToDTO(Advertisement ad) {
        AdvertisementDTO dto = new AdvertisementDTO();
        dto.setId(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDescription());
        dto.setAdType(ad.getAdType());
        dto.setPosition(ad.getPosition());
        dto.setFormat(ad.getFormat());
        dto.setImageUrl(ad.getImageUrl());
        dto.setHtmlContent(ad.getHtmlContent());
        dto.setScriptCode(ad.getScriptCode());
        dto.setTargetUrl(ad.getTargetUrl());
        dto.setOpenInNewTab(ad.isOpenInNewTab());
        dto.setWidth(ad.getWidth());
        dto.setHeight(ad.getHeight());
        dto.setStartDate(ad.getStartDate());
        dto.setEndDate(ad.getEndDate());
        dto.setActive(ad.isActive());
        dto.setPriority(ad.getPriority());
        dto.setImpressions(ad.getImpressions());
        dto.setClicks(ad.getClicks());
        dto.setDisplayFrequency(ad.getDisplayFrequency());
        dto.setMaxDailyImpressions(ad.getMaxDailyImpressions());
        dto.setCreatedAt(ad.getCreatedAt());
        dto.setUpdatedAt(ad.getUpdatedAt());
        return dto;
    }
}

