'use client';

import Link from 'next/link';
import Image from 'next/image';
import { News } from '@/types';
import InFeedAd from './ads/InFeedAd';

interface LatestNewsWithAdsProps {
  news: News[];
  adFrequency?: number; // Show ad every N posts
}

export default function LatestNewsWithAds({ news, adFrequency = 10 }: LatestNewsWithAdsProps) {
  if (!news || news.length === 0) {
    return null;
  }

  const renderNewsWithAds = () => {
    const items = [];
    
    for (let i = 0; i < news.length; i++) {
      const item = news[i];
      
      // Add news item
      if (i < 2) {
        // Large cards for first 2
        items.push(
          <div key={`news-${item.id}`} className="news-card mb-4">
            <Link href={`/news/${item.slug}`} className="block h-[280px] relative">
              <Image
                src={item.imageUrl || '/placeholder.jpg'}
                alt={item.title}
                fill
                className="object-cover"
              />
            </Link>
            <div className="bg-gray-50 p-4">
              <div className="text-sm text-gray-600 mb-2">
                <Link href={`/category/${item.categoryId}`} className="hover:text-primary">
                  {item.categoryName}
                </Link>
                <span className="px-1">/</span>
                <span>{new Date(item.publishedAt).toLocaleDateString()}</span>
              </div>
              <Link href={`/news/${item.slug}`}>
                <h4 className="text-xl font-semibold mb-2 hover:text-primary transition-colors">
                  {item.title}
                </h4>
              </Link>
              <p className="text-gray-600 line-clamp-2">{item.excerpt}</p>
            </div>
          </div>
        );
      } else {
        // Small cards for rest
        items.push(
          <div key={`news-${item.id}`} className="flex space-x-3 mb-4">
            <div className="relative w-24 h-24 flex-shrink-0">
              <Image
                src={item.imageUrl || '/placeholder.jpg'}
                alt={item.title}
                fill
                className="object-cover rounded"
              />
            </div>
            <div className="flex-1 bg-gray-50 p-3 rounded">
              <div className="text-xs text-gray-600 mb-1">
                <Link href={`/category/${item.categoryId}`} className="hover:text-primary">
                  {item.categoryName}
                </Link>
                <span className="px-1">/</span>
                <span>{new Date(item.publishedAt).toLocaleDateString()}</span>
              </div>
              <Link href={`/news/${item.slug}`}>
                <h5 className="text-sm font-semibold hover:text-primary transition-colors line-clamp-2">
                  {item.title}
                </h5>
              </Link>
            </div>
          </div>
        );
      }
      
      // Insert ad every adFrequency posts (e.g., every 10 posts)
      if ((i + 1) % adFrequency === 0 && i < news.length - 1) {
        items.push(
          <InFeedAd key={`ad-${i}`} className="my-6" />
        );
      }
    }
    
    return items;
  };

  return (
    <div>
      <div className="flex items-center justify-between bg-gray-100 py-2 px-4 mb-4">
        <h3 className="text-xl font-bold">Latest News</h3>
        <Link href="/latest" className="text-primary text-sm font-medium">
          View All
        </Link>
      </div>

      <div className="space-y-4">
        {renderNewsWithAds()}
      </div>
    </div>
  );
}

