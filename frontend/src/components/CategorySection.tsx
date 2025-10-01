'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { newsApi } from '@/lib/api';
import { News, Category } from '@/types';

interface CategorySectionProps {
  category: Category;
}

export default function CategorySection({ category }: CategorySectionProps) {
  const [news, setNews] = useState<News[]>([]);

  useEffect(() => {
    newsApi.getByCategory(category.id, 0, 3).then((res) => {
      setNews(res.data.content || []);
    }).catch(console.error);
  }, [category.id]);

  if (news.length === 0) {
    return null;
  }

  return (
    <div className="mb-8">
      <div className="bg-gray-100 py-2 px-4 mb-4">
        <h3 className="text-xl font-bold">{category.name}</h3>
      </div>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {news.map((item) => (
          <div key={item.id} className="news-card">
            <Link href={`/news/${item.slug}`} className="block h-[200px] relative">
              <Image
                src={item.imageUrl || '/placeholder.jpg'}
                alt={item.title}
                fill
                className="object-cover"
              />
            </Link>
            <div className="bg-gray-50 p-3">
              <div className="text-xs text-gray-600 mb-2">
                <Link href={`/category/${category.slug}`} className="hover:text-primary">
                  {item.categoryName}
                </Link>
                <span className="px-1">/</span>
                <span>{new Date(item.publishedAt).toLocaleDateString()}</span>
              </div>
              <Link href={`/news/${item.slug}`}>
                <h4 className="text-lg font-semibold hover:text-primary transition-colors line-clamp-2">
                  {item.title}
                </h4>
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

