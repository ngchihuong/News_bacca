'use client';

import Link from 'next/link';
import Image from 'next/image';
import { News } from '@/types';

interface FeaturedNewsProps {
  news: News[];
}

export default function FeaturedNews({ news }: FeaturedNewsProps) {
  if (!news || news.length === 0) {
    return null;
  }

  return (
    <div className="mb-8">
      <div className="flex items-center justify-between bg-gray-100 py-2 px-4 mb-4">
        <h3 className="text-xl font-bold">Featured</h3>
        <Link href="/featured" className="text-primary text-sm font-medium">
          View All
        </Link>
      </div>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {news.slice(0, 4).map((item) => (
          <Link key={item.id} href={`/news/${item.slug}`} className="news-card block h-[300px]">
            <div className="relative h-full">
              <Image
                src={item.imageUrl || '/placeholder.jpg'}
                alt={item.title}
                fill
                className="object-cover"
              />
              <div className="overlay">
                <div className="mb-2 text-xs">
                  <span className="text-white">{item.categoryName}</span>
                  <span className="px-1 text-white">/</span>
                  <span className="text-white">
                    {new Date(item.publishedAt).toLocaleDateString()}
                  </span>
                </div>
                <h4 className="text-base font-bold text-white line-clamp-2">
                  {item.title}
                </h4>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

