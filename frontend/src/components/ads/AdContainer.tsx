'use client';

import { useEffect, useState } from 'react';
import { Advertisement, AdPosition } from '@/types/advertisement';
import { advertisementApi } from '@/lib/adApi';
import AdUnit from './AdUnit';

interface AdContainerProps {
  position: AdPosition;
  className?: string;
}

export default function AdContainer({ position, className = '' }: AdContainerProps) {
  const [ads, setAds] = useState<Advertisement[]>([]);
  const [currentAdIndex, setCurrentAdIndex] = useState(0);

  useEffect(() => {
    fetchAds();
  }, [position]);

  const fetchAds = async () => {
    try {
      const response = await advertisementApi.getByPosition(position);
      setAds(response.data || []);
    } catch (error) {
      console.error('Error fetching ads:', error);
    }
  };

  if (ads.length === 0) return null;

  // Rotate ads if multiple available
  const currentAd = ads[currentAdIndex % ads.length];

  return (
    <div className={`ad-container ${className}`}>
      <AdUnit
        ad={currentAd}
        onImpression={() => {
          // Rotate to next ad after impression
          setTimeout(() => {
            setCurrentAdIndex((prev) => prev + 1);
          }, 30000); // Rotate every 30 seconds
        }}
      />
    </div>
  );
}

