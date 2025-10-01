package com.newsroom.controller;

import com.newsroom.dto.AdvertisementDTO;
import com.newsroom.service.IAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdvertisementController {
    
    private final IAdvertisementService advertisementService;
    
    @GetMapping("/position/{position}")
    public ResponseEntity<List<AdvertisementDTO>> getAdsByPosition(@PathVariable String position) {
        List<AdvertisementDTO> ads = advertisementService.getAdvertisementsByPosition(position);
        return ResponseEntity.ok(ads);
    }
    
    @PostMapping("/{id}/impression")
    public ResponseEntity<Void> trackImpression(@PathVariable String id) {
        advertisementService.incrementImpression(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/click")
    public ResponseEntity<Void> trackClick(@PathVariable String id) {
        advertisementService.incrementClick(id);
        return ResponseEntity.ok().build();
    }
}

