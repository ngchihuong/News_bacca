'use client';

import Link from 'next/link';
import Image from 'next/image';
import { News, Category } from '@/types';

interface MainSliderProps {
  news: News[];
  categories: Category[];
}

export default function MainSlider({ news, categories }: MainSliderProps) {
  const mainNews = news[0];

  if (!mainNews) {
    return null;
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 mb-6">
      {/* Main Slider */}
      <div className="lg:col-span-2">
        <Link href={`/news/${mainNews.slug}`} className="news-card block h-[435px]">
          <div className="relative h-full">
            <Image
              src={mainNews.imageUrl || '/placeholder.jpg'}
              alt={mainNews.title}
              fill
              className="object-cover"
            />
            <div className="overlay">
              <div className="mb-2">
                <span className="text-white">{mainNews.categoryName}</span>
                <span className="px-2 text-white">/</span>
                <span className="text-white">
                  {new Date(mainNews.publishedAt).toLocaleDateString()}
                </span>
              </div>
              <h2 className="text-2xl font-bold text-white">{mainNews.title}</h2>
            </div>
          </div>
        </Link>
      </div>

      {/* Categories */}
      <div className="space-y-3">
        <div className="flex items-center justify-between bg-gray-100 py-2 px-4">
          <h3 className="text-xl font-bold">Categories</h3>
          <Link href="/categories" className="text-primary text-sm font-medium">
            View All
          </Link>
        </div>
        {categories.map((category) => (
          <Link
            key={category.id}
            href={`/category/${category.slug}`}
            className="relative block h-20 overflow-hidden rounded"
          >
            <div className="relative h-full">
              <Image
                src={category.imageUrl || '/placeholder.jpg'}
                alt={category.name}
                fill
                className="object-cover"
              />
              <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                <h4 className="text-xl font-bold text-white">{category.name}</h4>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

