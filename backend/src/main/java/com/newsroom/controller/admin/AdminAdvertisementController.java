package com.newsroom.controller.admin;

import com.newsroom.dto.AdvertisementDTO;
import com.newsroom.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/advertisements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
public class AdminAdvertisementController {
    
    private final AdvertisementService advertisementService;
    
    @GetMapping
    public ResponseEntity<List<AdvertisementDTO>> getAllAdvertisements() {
        List<AdvertisementDTO> ads = advertisementService.getAllAdvertisements();
        return ResponseEntity.ok(ads);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> getAdvertisementById(@PathVariable String id) {
        AdvertisementDTO ad = advertisementService.getAdvertisementById(id);
        return ResponseEntity.ok(ad);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdvertisementDTO> createAdvertisement(@Valid @RequestBody AdvertisementDTO dto) {
        AdvertisementDTO createdAd = advertisementService.createAdvertisement(dto);
        return new ResponseEntity<>(createdAd, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdvertisementDTO> updateAdvertisement(
            @PathVariable String id,
            @Valid @RequestBody AdvertisementDTO dto) {
        AdvertisementDTO updatedAd = advertisementService.updateAdvertisement(id, dto);
        return ResponseEntity.ok(updatedAd);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable String id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AdvertisementDTO>> getActiveAdvertisements() {
        List<AdvertisementDTO> ads = advertisementService.getActiveAdvertisements();
        return ResponseEntity.ok(ads);
    }
}

