package com.newsroom.repository;

import com.newsroom.model.Advertisement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {
    List<Advertisement> findByPosition(String position);
    
    List<Advertisement> findByPositionAndActiveTrue(String position);
    
    List<Advertisement> findByActiveTrue();
    
    List<Advertisement> findByActiveTrueAndStartDateBeforeAndEndDateAfter(
        LocalDateTime now1, LocalDateTime now2
    );
    
    List<Advertisement> findByActiveTrueAndPositionAndStartDateBeforeAndEndDateAfter(
        String position, LocalDateTime now1, LocalDateTime now2
    );
    
    List<Advertisement> findByActiveTrueOrderByPriorityDesc();
}

