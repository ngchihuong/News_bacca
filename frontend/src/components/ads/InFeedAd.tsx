'use client';

import { useEffect, useState } from 'react';
import { Advertisement } from '@/types/advertisement';
import { advertisementApi } from '@/lib/adApi';
import AdUnit from './AdUnit';

interface InFeedAdProps {
  className?: string;
}

export default function InFeedAd({ className = '' }: InFeedAdProps) {
  const [ad, setAd] = useState<Advertisement | null>(null);

  useEffect(() => {
    fetchAd();
  }, []);

  const fetchAd = async () => {
    try {
      const response = await advertisementApi.getByPosition('IN_FEED');
      const ads = response.data || [];
      if (ads.length > 0) {
        // Random ad from available
        const randomAd = ads[Math.floor(Math.random() * ads.length)];
        setAd(randomAd);
      }
    } catch (error) {
      console.error('Error fetching in-feed ad:', error);
    }
  };

  if (!ad) return null;

  return (
    <div className={`in-feed-ad my-6 ${className}`}>
      <div className="max-w-2xl mx-auto">
        <AdUnit ad={ad} />
      </div>
    </div>
  );
}

